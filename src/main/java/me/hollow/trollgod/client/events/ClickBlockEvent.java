package me.hollow.trollgod.client.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import tcb.bces.event.EventCancellable;

public class ClickBlockEvent
extends EventCancellable {
    final BlockPos pos;
    final EnumFacing facing;
    final int stage;

    public ClickBlockEvent(int stage, BlockPos pos, EnumFacing facing) {
        this.stage = stage;
        this.pos = pos;
        this.facing = facing;
    }

    public final BlockPos getPos() {
        return this.pos;
    }

    public final EnumFacing getFacing() {
        return this.facing;
    }

    public final int getStage() {
        return this.stage;
    }
}

