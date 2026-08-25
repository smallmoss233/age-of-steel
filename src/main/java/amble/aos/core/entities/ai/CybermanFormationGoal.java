package amble.aos.core.entities.ai;

import amble.aos.core.entities.CybermanBase;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class CybermanFormationGoal extends Goal {
    private static final double GROUP_RADIUS = 32.0D;
    private static final double SPACING = 2.5D;
    private static final int MIN_GROUP = 3;
    private static final double ARRIVED_SQ = 1.0D;
    private static final double SPRINT_SLOT_SQ = 9.0D;
    private static final int RECALC_INTERVAL = 5;
    /** Within this range of the target a follower breaks formation to engage individually. */
    private static final double PERSONAL_ENGAGE_SQ = 12.0D * 12.0D;
    private static final double SPRINT = 1.9D;

    private final CybermanBase mob;
    private final double speedModifier;

    private CybermanBase leader;
    private Vec3 slot;
    private int recalc;

    public CybermanFormationGoal(CybermanBase mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.personallyEngaging()) {
            return false;
        }
        List<CybermanBase> group = this.findGroup();
        if (group.size() < MIN_GROUP) {
            return false;
        }
        CybermanBase candidate = group.get(0);
        if (candidate == this.mob) {
            return false;
        }
        this.leader = candidate;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.leader != null
                && this.leader.isAlive()
                && !this.personallyEngaging()
                && this.mob.distanceToSqr(this.leader) < (GROUP_RADIUS * 2.0D) * (GROUP_RADIUS * 2.0D);
    }

    @Override
    public void start() {
        this.mob.setMarching(true);
    }

    @Override
    public void stop() {
        this.leader = null;
        this.slot = null;
        this.recalc = 0;
        this.mob.setMarching(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.leader == null) {
            return;
        }

        if (--this.recalc <= 0) {
            this.recalc = RECALC_INTERVAL;
            List<CybermanBase> group = this.findGroup();
            int rank = group.indexOf(this.mob);
            if (rank < 0) {
                rank = group.size();
            }
            this.slot = this.computeSlot(this.leader, rank);

            double distToSlotSq = this.mob.distanceToSqr(this.slot.x, this.slot.y, this.slot.z);
            if (distToSlotSq > ARRIVED_SQ) {
                double speed = distToSlotSq > SPRINT_SLOT_SQ ? SPRINT : this.speedModifier;
                this.mob.getNavigation().moveTo(this.slot.x, this.slot.y, this.slot.z, speed);
            } else {
                this.mob.getNavigation().stop();
            }
        }

        if (this.slot != null && this.mob.distanceToSqr(this.slot.x, this.slot.y, this.slot.z) <= ARRIVED_SQ) {
            this.mob.yBodyRot = this.leader.yBodyRot;
            this.mob.setYHeadRot(this.leader.yBodyRot);
        }
    }

    private boolean personallyEngaging() {
        LivingEntity target = this.mob.getTarget();
        return target != null && this.mob.distanceToSqr(target) <= PERSONAL_ENGAGE_SQ;
    }

    private List<CybermanBase> findGroup() {
        List<CybermanBase> group = this.mob.level().getEntitiesOfClass(
                CybermanBase.class,
                this.mob.getBoundingBox().inflate(GROUP_RADIUS),
                Entity::isAlive);
        group.sort(Comparator.comparing(Entity::getUUID));
        return group;
    }

    /**
     * Places rank 0 (the leader) at the tip and stacks the rest into rows of increasing
     * width behind it: row r holds r+1 slots, so 3 cybermen form a chevron and more grow a
     * racked triangle. The wedge points at the shared target when chasing, else the leader's
     * heading.
     */
    private Vec3 computeSlot(CybermanBase formationLeader, int rank) {
        int row = 0;
        int indexInRow = rank;
        while (indexInRow > row) {
            indexInRow -= (row + 1);
            row++;
        }

        double back = row * SPACING;
        double lateral = (indexInRow - row / 2.0D) * SPACING;

        Vec3 forward = this.formationForward(formationLeader);
        Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);

        return formationLeader.position()
                .subtract(forward.scale(back))
                .add(right.scale(lateral));
    }

    private Vec3 formationForward(CybermanBase formationLeader) {
        LivingEntity leaderTarget = formationLeader.getTarget();
        if (leaderTarget != null) {
            Vec3 toTarget = new Vec3(
                    leaderTarget.getX() - formationLeader.getX(),
                    0.0D,
                    leaderTarget.getZ() - formationLeader.getZ());
            if (toTarget.lengthSqr() > 1.0E-4D) {
                return toTarget.normalize();
            }
        }
        float yaw = formationLeader.yBodyRot * ((float) Math.PI / 180F);
        return new Vec3(-Mth.sin(yaw), 0.0D, Mth.cos(yaw));
    }
}
