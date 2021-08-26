package me.hollow.trollgod.api.util;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.interfaces.Minecraftable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;

import javax.annotation.Nullable;

public final class EntityUtil
implements Minecraftable {
    private static final DamageSource EXPLOSION_SOURCE = new DamageSource("explosion").setDifficultyScaled().setExplosion();

    public static boolean isntValid(EntityPlayer entity, double range) {
        return (double)EntityUtil.mc.player.getDistance( entity ) > range || entity == EntityUtil.mc.player || entity.getHealth() <= 0.0f || entity.isDead || TrollGod.INSTANCE.getFriendManager().isFriend(entity);
    }

    public static boolean isntValidFriend(EntityPlayer entity, double range) {
        return (double)EntityUtil.mc.player.getDistance( entity ) > range || entity == EntityUtil.mc.player || entity.getHealth() <= 0.0f || entity.isDead || !TrollGod.INSTANCE.getFriendManager().isFriend(entity);
    }

    public static float getHealth(EntityLivingBase player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public static float calculate(double posX, double posY, double posZ, EntityLivingBase entity) {
        double v = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0) * (double)EntityUtil.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
        return EntityUtil.getBlastReduction(entity, EntityUtil.getDamageMultiplied((float)((v * v + v) / 2.0 * 85.0 + 1.0)));
    }

    public static float getBlastReduction(EntityLivingBase entity, float damageI) {
        float damage = damageI;
        damage = CombatRules.getDamageAfterAbsorb( damage , (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue() );
        damage *= 1.0f - MathHelper.clamp((float)EnchantmentHelper.getEnchantmentModifierDamage( entity.getArmorInventoryList() , EXPLOSION_SOURCE ), 0.0f , 20.0f ) / 25.0f;
        if (entity.isPotionActive(MobEffects.RESISTANCE)) {
            return damage - damage / 4.0f;
        }
        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = EntityUtil.mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float getBlockDensity(Vec3d vec, AxisAlignedBB bb) {
        double d0 = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        double d1 = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        double d2 = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        double d3 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        double d4 = (1.0 - Math.floor(1.0 / d2) * d2) / 2.0;
        float j2 = 0.0f;
        float k2 = 0.0f;
        float f = 0.0f;
        while (f <= 1.0f) {
            float f1 = 0.0f;
            while (f1 <= 1.0f) {
                float f2 = 0.0f;
                while (f2 <= 1.0f) {
                    double d5 = bb.minX + (bb.maxX - bb.minX) * (double)f;
                    double d6 = bb.minY + (bb.maxY - bb.minY) * (double)f1;
                    double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double)f2;
                    if (EntityUtil.rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec, false, false, false) == null) {
                        j2 += 1.0f;
                    }
                    k2 += 1.0f;
                    f2 = (float)((double)f2 + d2);
                }
                f1 = (float)((double)f1 + d1);
            }
            f = (float)((double)f + d0);
        }
        return j2 / k2;
    }

    @Nullable
    public static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        int i = MathHelper.floor( vec32.x );
        int j = MathHelper.floor( vec32.y );
        int k = MathHelper.floor( vec32.z );
        int l = MathHelper.floor( vec31.x );
        int i1 = MathHelper.floor( vec31.y );
        int j1 = MathHelper.floor( vec31.z );
        BlockPos blockpos = new BlockPos(l, i1, j1);
        IBlockState iblockstate = EntityUtil.mc.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox( EntityUtil.mc.world , blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
            return iblockstate.collisionRayTrace( EntityUtil.mc.world , blockpos, vec31, vec32);
        }
        RayTraceResult raytraceresult2 = null;
        int k1 = 200;
        while (k1-- >= 0) {
            EnumFacing enumfacing;
            if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                return null;
            }
            if (l == i && i1 == j && j1 == k) {
                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            boolean flag2 = true;
            boolean flag = true;
            boolean flag1 = true;
            double d0 = 999.0;
            double d1 = 999.0;
            double d2 = 999.0;
            if (i > l) {
                d0 = (double)l + 1.0;
            } else if (i < l) {
                d0 = (double)l + 0.0;
            } else {
                flag2 = false;
            }
            if (j > i1) {
                d1 = (double)i1 + 1.0;
            } else if (j < i1) {
                d1 = (double)i1 + 0.0;
            } else {
                flag = false;
            }
            if (k > j1) {
                d2 = (double)j1 + 1.0;
            } else if (k < j1) {
                d2 = (double)j1 + 0.0;
            } else {
                flag1 = false;
            }
            double d3 = 999.0;
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = vec32.x - vec31.x;
            double d7 = vec32.y - vec31.y;
            double d8 = vec32.z - vec31.z;
            if (flag2) {
                d3 = (d0 - vec31.x) / d6;
            }
            if (flag) {
                d4 = (d1 - vec31.y) / d7;
            }
            if (flag1) {
                d5 = (d2 - vec31.z) / d8;
            }
            if (d3 == -0.0) {
                d3 = -1.0E-4;
            }
            if (d4 == -0.0) {
                d4 = -1.0E-4;
            }
            if (d5 == -0.0) {
                d5 = -1.0E-4;
            }
            if (d3 < d4 && d3 < d5) {
                enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
            } else if (d4 < d5) {
                enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
            } else {
                enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
            }
            l = MathHelper.floor( vec31.x ) - (enumfacing == EnumFacing.EAST ? 1 : 0);
            i1 = MathHelper.floor( vec31.y ) - (enumfacing == EnumFacing.UP ? 1 : 0);
            j1 = MathHelper.floor( vec31.z ) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
            blockpos = new BlockPos(l, i1, j1);
            IBlockState iblockstate1 = EntityUtil.mc.world.getBlockState(blockpos);
            Block block1 = iblockstate1.getBlock();
            if (ignoreBlockWithoutBoundingBox && iblockstate1.getMaterial() != Material.PORTAL && iblockstate1.getCollisionBoundingBox( EntityUtil.mc.world , blockpos) == Block.NULL_AABB) continue;
            if (block1.canCollideCheck(iblockstate1, stopOnLiquid) && !(block1 instanceof BlockWeb)) {
                return iblockstate1.collisionRayTrace( EntityUtil.mc.world , blockpos, vec31, vec32);
            }
            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
        }
        return returnLastUncollidableBlock ? raytraceresult2 : null;
    }
}

