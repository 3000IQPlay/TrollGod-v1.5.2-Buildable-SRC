package me.hollow.trollgod.api.mixin.mixins.render;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={EntityRenderer.class})
public interface AccessorEntityRenderer {
    @Accessor(value="drawBlockOutline")
    void setDrawBlockOutline ( boolean var1 );

    @Invoker(value="orientCamera")
    void invokeOrientCamera ( float var1 );
}

