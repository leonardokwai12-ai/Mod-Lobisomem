package com.lobisomem.mod.item;

import com.lobisomem.mod.entity.WerewolfEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RifleItem extends Item {
    public RifleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack rifleStack = user.getStackInHand(hand);
        ItemStack bulletStack = findBullet(user);

        if (!bulletStack.isEmpty() || user.getAbilities().creativeMode) {
            if (!world.isClient) {
                if (!user.getAbilities().creativeMode) {
                    bulletStack.decrement(1);
                }

                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.8f, 1.8f);

                double range = 40.0;
                Vec3d start = user.getCameraPosVec(1.0f);
                Vec3d direction = user.getRotationVec(1.0f);
                Vec3d end = start.add(direction.multiply(range));
                Box box = user.getBoundingBox().stretch(direction.multiply(range)).expand(1.0);

                EntityHitResult hitResult = ProjectileUtil.raycast(user, start, end, box, e -> !e.isSpectator() && e.canHit(), range * range);

                if (hitResult != null) {
                    Entity target = hitResult.getEntity();
                    target.damage(user.getDamageSources().playerAttack(user), 10.0f);

                    if (target instanceof WerewolfEntity werewolf) {
                        werewolf.scare(user, 200);
                    }
                }
            }
            user.getItemCooldownManager().set(this, 15);
            return TypedActionResult.success(rifleStack);
        }

        return TypedActionResult.fail(rifleStack);
    }

    private ItemStack findBullet(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(ModItems.STONE_BULLET)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}