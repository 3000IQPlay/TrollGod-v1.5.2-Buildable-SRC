package me.hollow.trollgod.client.modules.visual;

import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.api.util.BlockUtil;
import me.hollow.trollgod.api.util.render.RenderUtil;
import me.hollow.trollgod.client.events.UpdateEvent;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import tcb.bces.listener.Subscribe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleManifest(label="VoidESP", category=Module.Category.VISUAL, color=-5602)
public class VoidESP
extends Module {
    private final Setting<Float> range = this.register( new Setting <> ( "Range" , 6.0f , 3.0f , 16.0f ));
    private final Setting<Boolean> down = this.register( new Setting <> ( "Up" , false ));
    private List<BlockPos> holes = new ArrayList <> ( );

    @Subscribe
    public void onTick(UpdateEvent event) {
        if (this.isNull()) {
            return;
        }
        this.holes = this.calcHoles();
    }

    @Override
    public void onRender3D() {
        int size = this.holes.size();
        for (BlockPos pos : this.holes) {
            RenderUtil.renderCrosses ( this.down.getValue ( ) ? pos.up ( ) : pos , new Color ( 255 , 255 , 255 ) , 2.0f );
        }
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> voidHoles = new ArrayList <> ( );
        List<BlockPos> positions = BlockUtil.getSphere( this.range.getValue ( ) , false);
        int size = positions.size();
        for (BlockPos pos : positions) {
            if ( pos.getY ( ) != 0 || this.mc.world.getBlockState ( pos ).getBlock ( ) == Blocks.BEDROCK ) continue;
            voidHoles.add ( pos );
        }
        return voidHoles;
    }
}

