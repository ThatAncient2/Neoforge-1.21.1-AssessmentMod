package net.thatancient2.assessmentprototypemod.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.EnumSet;

public class GupEntity extends Mob implements Enemy {

    public GupEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new GupMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 8);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GupEntity.GupFloatGoal(this));
        this.goalSelector.addGoal(2, new GupEntity.GupAttackGoal(this));
        this.goalSelector.addGoal(3, new GupEntity.GupRandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new GupEntity.GupKeepOnJumpingGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true, false));
    }

    protected int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }

    protected float getSoundPitch() {
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8f;
    }

    protected float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    protected void dealDamage(LivingEntity livingEntity) {
        if (this.isAlive() && this.isWithinMeleeAttackRange(livingEntity) && this.hasLineOfSight(livingEntity)) {
            DamageSource damagesource = this.damageSources().mobAttack(this);
            if (livingEntity.hurt(damagesource, this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                Level var4 = this.level();
                if (var4 instanceof ServerLevel) {
                    ServerLevel serverlevel = (ServerLevel)var4;
                    EnchantmentHelper.doPostAttackEffects(serverlevel, livingEntity, damagesource);
                }
            }
        }
    }

    public void playerTouch(Player entity) {
        this.dealDamage(entity);
    }

    public void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
        CommonHooks.onLivingJump(this);
    }

    static class GupFloatGoal extends Goal {
        private final GupEntity gup;

        public GupFloatGoal(GupEntity gup) {
            this.gup = gup;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            gup.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return (this.gup.isInWater() || this.gup.isInLava()) && this.gup.getMoveControl() instanceof GupMoveControl;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.gup.getRandom().nextFloat() < 0.8F) {
                this.gup.getJumpControl().jump();
            }

            MoveControl var2 = this.gup.getMoveControl();
            if (var2 instanceof GupMoveControl gupMoveControl) {
                gupMoveControl.setWantedMovement(2.4);
            }

        }
    }

    static class GupKeepOnJumpingGoal extends Goal {
        private final GupEntity gup;

        public GupKeepOnJumpingGoal(GupEntity gup) {
            this.gup = gup;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            return !this.gup.isPassenger();
        }

        public void tick() {
            MoveControl var2 = this.gup.getMoveControl();
            if (var2 instanceof GupMoveControl gupMoveControl) {
                gupMoveControl.setWantedMovement(2.0F);
            }

        }
    }

    static class GupAttackGoal extends Goal {
        private final GupEntity gup;
        private int growTiredTimer;

        public GupAttackGoal(GupEntity gup) {
            this.gup = gup;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.gup.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return this.gup.canAttack(livingentity) && this.gup.getMoveControl() instanceof GupMoveControl;
            }
        }

        public void start() {
            this.growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.gup.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return this.gup.canAttack(livingentity) && --this.growTiredTimer > 0;
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.gup.getTarget();
            if (livingentity != null) {
                this.gup.lookAt(livingentity, 10.0F, 10.0F);
            }

            MoveControl var3 = this.gup.getMoveControl();
            if (var3 instanceof GupMoveControl gupMoveControl) {
                gupMoveControl.setDirection(this.gup.getYRot(), true);
            }

        }
    }

    static class GupMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final GupEntity gup;
        private boolean isAggressive;

    public GupMoveControl(GupEntity gup) {
        super(gup);
        this.gup = gup;
        this.yRot = 180.0F * gup.getYRot() / (float)Math.PI;
    }

    public void setDirection(float yRot, boolean aggressive) {
        this.yRot = yRot;
        this.isAggressive = aggressive;
    }

    public void setWantedMovement(double speed) {
        this.speedModifier = speed;
        this.operation = MoveControl.Operation.MOVE_TO;
    }

    public void tick() {
        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
        this.mob.yHeadRot = this.mob.getYRot();
        this.mob.yBodyRot = this.mob.getYRot();
        if (this.operation != Operation.MOVE_TO) {
            this.mob.setZza(0.0F);
        } else {
            this.operation = Operation.WAIT;
            if (this.mob.onGround()) {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.jumpDelay-- <= 0) {
                    this.jumpDelay = this.gup.getJumpDelay();
                    if (this.isAggressive) {
                        this.jumpDelay /= 3;
                    }

                    this.gup.getJumpControl().jump();
                    this.gup.playSound(SoundEvents.SLIME_JUMP, this.gup.getSoundVolume(), this.gup.getSoundPitch());
                } else {
                    this.gup.xxa = 0.0F;
                    this.gup.zza = 0.0F;
                    this.mob.setSpeed(0.0F);
                }
            } else {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
        }

    }
    }

    static class GupRandomDirectionGoal extends Goal {
        private final GupEntity gup;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public GupRandomDirectionGoal(GupEntity gup) {
            this.gup = gup;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            return this.gup.getTarget() == null && (this.gup.onGround() || this.gup.isInWater() || this.gup.isInLava() || this.gup.hasEffect(MobEffects.LEVITATION)) && this.gup.getMoveControl() instanceof GupMoveControl;
        }

        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.gup.getRandom().nextInt(60));
                this.chosenDegrees = (float)this.gup.getRandom().nextInt(360);
            }

            MoveControl var2 = this.gup.getMoveControl();
            if (var2 instanceof GupMoveControl gupMoveControl) {
                gupMoveControl.setDirection(this.chosenDegrees, false);
            }

        }
    }
}
