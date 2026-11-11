package com.lobisomem.mod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WerewolfEntity extends HostileEntity {
    private int fleeTicks = 0;
    private LivingEntity threat = null;

    public WerewolfEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createWerewolfAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.38)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public void scare(LivingEntity shooter, int ticks) {
        this.threat = shooter;
        this.fleeTicks = ticks;
        this.setTarget(null);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            if (this.getWorld().isDay() && this.getWorld().isSkyVisible(this.getBlockPos())) {
                this.discard();
                return;
            }

            if (this.fleeTicks > 0) {
                this.fleeTicks--;
                this.setTarget(null);
                if (this.threat != null) {
                    Vec3d away = this.getPos().subtract(this.threat.getPos()).normalize();
                    this.getNavigation().startMovingTo(this.getX() + away.x * 16, this.getY(), this.getZ() + away.z * 16, 1.6);
                }
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (this.fleeTicks > 0) {
            return false;
        }
        boolean hit = super.tryAttack(target);
        if (hit && target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 4));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 4));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 4));
        }
        return hit;
    }
}