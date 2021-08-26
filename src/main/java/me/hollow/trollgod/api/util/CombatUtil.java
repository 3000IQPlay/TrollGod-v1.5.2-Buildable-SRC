package me.hollow.trollgod.api.util;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CombatUtil
implements Minecraftable {
    public static EntityPlayer getTarget(float range) {
        EntityPlayer currentTarget = null;
        int size = CombatUtil.mc.world.playerEntities.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer player = CombatUtil.mc.world.playerEntities.get(i);
            if (EntityUtil.isntValid(player, range)) continue;
            if (currentTarget == null) {
                currentTarget = player;
                continue;
            }
            if (!(CombatUtil.mc.player.getDistanceSq( player ) < CombatUtil.mc.player.getDistanceSq( currentTarget ))) continue;
            currentTarget = player;
        }
        return currentTarget;
    }

    public static boolean isInHole(EntityPlayer entity) {
        return CombatUtil.isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return CombatUtil.isBedrockHole(blockPos) || CombatUtil.isObbyHole(blockPos) || CombatUtil.isBothHole(blockPos);
    }

    public static int isInHoleInt(EntityPlayer entity) {
        BlockPos playerPos = new BlockPos(entity.getPositionVector());
        if (CombatUtil.isBedrockHole(playerPos)) {
            return 1;
        }
        if (CombatUtil.isObbyHole(playerPos) || CombatUtil.isBothHole(playerPos)) {
            return 2;
        }
        return 0;
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = CombatUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = CombatUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isAir(BlockPos pos) {
        return CombatUtil.mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    public static boolean is2x1(BlockPos pos) {
        if (isAir(pos) && isAir(pos.up()) && isAir(pos.up(2)) && !isAir(pos.down())) {
            int airBlocks = 0;
            EnumFacing[] var2 = EnumFacing.HORIZONTALS;

            for (EnumFacing facing : var2) {
                BlockPos offset = pos.offset ( facing );
                if ( isAir ( offset ) && isAir ( offset.up ( ) ) ) {
                    if ( isAir ( offset.down ( ) ) ) {
                        return false;
                    }

                    EnumFacing[] var7 = EnumFacing.HORIZONTALS;

                    for (EnumFacing offsetFacing : var7) {
                        if ( offsetFacing != facing.getOpposite ( ) && isAir ( offset.offset ( offsetFacing ) ) ) {
                            return false;
                        }
                    }

                    ++ airBlocks;
                }

                if ( airBlocks > 1 ) {
                    return false;
                }
            }

            return airBlocks == 1;
        } else {
            return false;
        }
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = CombatUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }
}

