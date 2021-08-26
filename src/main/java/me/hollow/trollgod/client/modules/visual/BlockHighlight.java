package me.hollow.trollgod.client.modules.visual;

import me.hollow.trollgod.api.mixin.mixins.render.AccessorEntityRenderer;
import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.api.util.render.RenderUtil;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import me.hollow.trollgod.client.modules.client.Colours;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

@ModuleManifest(label="BlockHighlight", listen=false, category=Module.Category.VISUAL, color=-1070353)
public final class BlockHighlight
extends Module {
    private final Setting<Float> lineWidth = this.register( new Setting <> ( "Width" , 1.0f , 0.1f , 4.0f ));
    private final Setting<Boolean> sync = this.register( new Setting <> ( "Sync" , true ));
    private final Setting<Integer> red = this.register( new Setting <> ( "Red" , 255 , 0 , 255 , v -> ! this.sync.getValue ( ) ));
    private final Setting<Integer> green = this.register( new Setting <> ( "Green" , 255 , 0 , 255 , v -> ! this.sync.getValue ( ) ));
    private final Setting<Integer> blue = this.register( new Setting <> ( "Blue" , 255 , 0 , 255 , v -> ! this.sync.getValue ( ) ));

    @Override
    public void onEnable() {
        ((AccessorEntityRenderer)this.mc.entityRenderer).setDrawBlockOutline(false);
    }

    @Override
    public void onDisable() {
        ((AccessorEntityRenderer)this.mc.entityRenderer).setDrawBlockOutline(true);
    }

    @Override
    public void onRender3D() {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            RenderUtil.renderProperOutline(this.mc.objectMouseOver.getBlockPos(), this.sync.getValue ( ) ? new Color(Colours.INSTANCE.getColor()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()), this.lineWidth.getValue ( ) );
        }
    }
}

