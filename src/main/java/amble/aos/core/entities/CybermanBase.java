package amble.aos.core.entities;

import amble.aos.core.AOSSounds;
import amble.aos.core.entities.ai.CybermanAttackGoal;
import amble.aos.core.entities.ai.CybermanFormationGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class CybermanBase extends Monster {
    public static final byte GUN_EVENT = 62;

    public static final float WALK_ANIM_SPEED = 5.0F;
    public static final float WALK_ANIM_AMPLITUDE = 3.0F;
    public static final float WALK_CADENCE = 1.5F;

    private static final float WALK_LENGTH = 2.3333F;
    private static final float SPEED_WALK_LENGTH = 2.0F;
    private static final float[] WALK_LEFT_PLANTS = {0.4167F, 1.5833F};
    private static final float[] WALK_RIGHT_PLANTS = {1.0F, 2.1667F};
    private static final float[] SPEED_WALK_LEFT_PLANTS = {0.4167F, 1.4167F};
    private static final float[] SPEED_WALK_RIGHT_PLANTS = {0.9167F, 1.9167F};
    private static final float WALK_MOVING_THRESHOLD = 0.025F;

    private static final float DELETE_START_LENGTH = 2.3333F;

    private static final EntityDataAccessor<Boolean> DATA_DELETING =
            SynchedEntityData.defineId(CybermanBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_MARCHING =
            SynchedEntityData.defineId(CybermanBase.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState gunAnimationState = new AnimationState();
    public final AnimationState deleteStartAnimationState = new AnimationState();
    public final AnimationState deleteLoopAnimationState = new AnimationState();

    private boolean wasDeleting;
    private float lastWalkPosition = Float.NaN;

    protected CybermanBase(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DELETING, false);
        this.entityData.define(DATA_MARCHING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CybermanFormationGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new CybermanAttackGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        NearestAttackableTargetGoal<Player> playerTarget = new NearestAttackableTargetGoal<>(this, Player.class, true);
        playerTarget.setUnseenMemoryTicks(400);
        this.targetSelector.addGoal(2, playerTarget);
        NearestAttackableTargetGoal<IronGolem> golemTarget = new NearestAttackableTargetGoal<>(this, IronGolem.class, true);
        golemTarget.setUnseenMemoryTicks(400);
        this.targetSelector.addGoal(3, golemTarget);
    }

    public boolean isChasing() {
        return this.getTarget() != null;
    }

    public void performMeleeAttack(LivingEntity target) {
        this.doHurtTarget(target);
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return BuiltInLootTables.EMPTY;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int lootingLevel, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, lootingLevel, recentlyHit);
        int count = 2 + this.random.nextInt(3) + this.random.nextInt(lootingLevel + 1);
        this.spawnAtLocation(new net.minecraft.world.item.ItemStack(Items.IRON_INGOT, count));
    }

    public void setDeleting(boolean deleting) {
        if (this.isDeleting() != deleting) {
            this.entityData.set(DATA_DELETING, deleting);
        }
    }

    public boolean isDeleting() {
        return this.entityData.get(DATA_DELETING);
    }

    public void setMarching(boolean marching) {
        if (this.isMarching() != marching) {
            this.entityData.set(DATA_MARCHING, marching);
        }
    }

    public boolean isMarching() {
        return this.entityData.get(DATA_MARCHING);
    }

    public void triggerGunAnimation() {
        this.level().broadcastEntityEvent(this, GUN_EVENT);
    }

    public void fireLaser(LivingEntity target) {
        if (this.level().isClientSide) {
            return;
        }

        Vec3 look = this.getLookAngle();
        double originY = this.getY() + this.getBbHeight() * 0.5D;
        Vec3 origin = new Vec3(this.getX(), originY, this.getZ()).add(look.x * 0.6D, 0.0D, look.z * 0.6D);

        Vec3 aim = new Vec3(
                target.getX() - origin.x,
                target.getY(0.5D) - origin.y,
                target.getZ() - origin.z);

        LaserBolt bolt = new LaserBolt(this.level(), this);
        bolt.setPos(origin.x, origin.y, origin.z);
        bolt.shoot(aim, 1.6F);
        this.level().addFreshEntity(bolt);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.updateDeletionAnimation();
            this.handleStompSounds();
        }
    }

    private void updateDeletionAnimation() {
        boolean deleting = this.isDeleting();
        if (deleting && !this.wasDeleting) {
            this.deleteStartAnimationState.start(this.tickCount);
            this.deleteLoopAnimationState.stop();
        } else if (!deleting && this.wasDeleting) {
            this.deleteStartAnimationState.stop();
            this.deleteLoopAnimationState.stop();
        }

        if (deleting
                && this.deleteStartAnimationState.isStarted()
                && this.deleteStartAnimationState.getAccumulatedTime() >= (long) (DELETE_START_LENGTH * 1000.0F)
                && !this.deleteLoopAnimationState.isStarted()) {
            this.deleteStartAnimationState.stop();
            this.deleteLoopAnimationState.start(this.tickCount);
        }

        this.wasDeleting = deleting;
    }

    private void handleStompSounds() {
        float position = this.walkAnimation.position();
        if (this.walkAnimation.speed() < WALK_MOVING_THRESHOLD) {
            this.lastWalkPosition = position;
            return;
        }

        boolean chasing = this.isChasing();
        float length = chasing ? SPEED_WALK_LENGTH : WALK_LENGTH;
        float[] leftPlants = chasing ? SPEED_WALK_LEFT_PLANTS : WALK_LEFT_PLANTS;
        float[] rightPlants = chasing ? SPEED_WALK_RIGHT_PLANTS : WALK_RIGHT_PLANTS;

        float current;
        float previous;
        if (this.isMarching()) {
            long gameTime = this.level().getGameTime();
            current = (float) ((gameTime * WALK_CADENCE / 20.0D) % length);
            previous = (float) (((gameTime - 1L) * WALK_CADENCE / 20.0D) % length);
        } else {
            current = (position * 0.05F * WALK_ANIM_SPEED) % length;
            if (Float.isNaN(this.lastWalkPosition)) {
                this.lastWalkPosition = position;
                return;
            }
            previous = (this.lastWalkPosition * 0.05F * WALK_ANIM_SPEED) % length;
        }

        if (crossedPlant(previous, current, leftPlants)) {
            this.playStomp();
        }
        if (crossedPlant(previous, current, rightPlants)) {
            this.playStomp();
        }

        this.lastWalkPosition = position;
    }

    private void playStomp() {
        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(),
                AOSSounds.MONDAS_STOMP, this.getSoundSource(), 1.0F, 1.0F, false);
    }

    private static boolean crossedPlant(float previous, float current, float[] plants) {
        for (float plant : plants) {
            boolean crossed = previous <= current
                    ? plant > previous && plant <= current
                    : plant > previous || plant <= current;
            if (crossed) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == GUN_EVENT) {
            this.gunAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    public String getLoc() {
        return "mondasian";
    }
}
