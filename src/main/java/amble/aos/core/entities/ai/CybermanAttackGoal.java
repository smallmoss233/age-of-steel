package amble.aos.core.entities.ai;

import amble.aos.core.entities.CybermanBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CybermanAttackGoal extends Goal {
    private static final double MELEE_MODE_SQ = 8.0D * 8.0D;
    private static final double GUN_MODE_SQ = 25.0D * 25.0D;
    private static final int MELEE_COOLDOWN = 47;
    private static final int RANGED_COOLDOWN = 100;
    private static final int RANGED_FIRE_TICK = 4;
    private static final int RANGED_RECOVER_TICK = 20;
    private static final int PATH_RECALC_INTERVAL = 10;
    private static final float GUN_CHANCE = 0.35F;
    private static final double SPRINT = 1.9D;
    private static final double MELEE_CONTACT_SQ = 6.25D;

    private final CybermanBase mob;
    private final double speedModifier;

    private LivingEntity target;
    private int meleeCooldown;
    private int rangedCooldown;
    private int rangedChargeTicks = -1;
    private int pathRecalcCooldown;
    private int seeTime;

    public CybermanAttackGoal(CybermanBase mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity candidate = this.mob.getTarget();
        if (candidate == null || !candidate.isAlive()) {
            return false;
        }
        this.target = candidate;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        this.mob.setAggressive(true);
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.rangedChargeTicks = -1;
        this.mob.setDeleting(false);
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity current = this.target;
        if (current == null) {
            return;
        }

        double distSq = this.mob.distanceToSqr(current.getX(), current.getY(), current.getZ());
        boolean canSee = this.mob.getSensing().hasLineOfSight(current);
        this.seeTime = canSee ? this.seeTime + 1 : 0;
        this.mob.getLookControl().setLookAt(current, 30.0F, 30.0F);

        double meleeReachSq = this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + current.getBbWidth();

        if (distSq <= MELEE_MODE_SQ) {
            this.rangedChargeTicks = -1;
            this.mob.setDeleting(true);
            if (distSq <= meleeReachSq && canSee) {
                this.mob.getNavigation().stop();
                if (this.meleeCooldown <= 0) {
                    this.meleeCooldown = MELEE_COOLDOWN;
                    this.mob.performMeleeAttack(current);
                }
            } else {
                this.pursue(current, distSq);
            }
        } else if (distSq <= GUN_MODE_SQ) {
            this.mob.setDeleting(false);
            if (this.rangedChargeTicks < 0) {
                if (canSee && this.seeTime >= 5 && this.rangedCooldown <= 0) {
                    if (this.mob.getRandom().nextFloat() < GUN_CHANCE) {
                        this.mob.getNavigation().stop();
                        this.rangedChargeTicks = 0;
                        this.mob.triggerGunAnimation();
                    } else {
                        this.rangedCooldown = RANGED_COOLDOWN / 2;
                        this.pursue(current, distSq);
                    }
                } else {
                    this.pursue(current, distSq);
                }
            }
        } else {
            this.mob.setDeleting(false);
            if (this.rangedChargeTicks < 0) {
                this.pursue(current, distSq);
            }
        }

        if (this.rangedChargeTicks >= 0) {
            this.mob.getNavigation().stop();
            this.rangedChargeTicks++;
            if (this.rangedChargeTicks == RANGED_FIRE_TICK && canSee) {
                this.mob.fireLaser(current);
            }
            if (this.rangedChargeTicks >= RANGED_RECOVER_TICK) {
                this.rangedChargeTicks = -1;
                this.rangedCooldown = RANGED_COOLDOWN;
            }
        }

        if (this.meleeCooldown > 0) {
            this.meleeCooldown--;
        }
        if (this.rangedCooldown > 0) {
            this.rangedCooldown--;
        }
    }

    private void pursue(LivingEntity current, double distSq) {
        if (--this.pathRecalcCooldown <= 0) {
            this.pathRecalcCooldown = PATH_RECALC_INTERVAL;
            double speed = distSq > MELEE_CONTACT_SQ ? SPRINT : this.speedModifier;
            this.mob.getNavigation().moveTo(current, speed);
        }
    }
}
