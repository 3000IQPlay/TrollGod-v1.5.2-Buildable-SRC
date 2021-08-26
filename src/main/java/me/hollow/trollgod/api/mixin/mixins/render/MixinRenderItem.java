package me.hollow.trollgod.api.mixin.mixins.render;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import me.hollow.trollgod.client.modules.visual.EnchantColor;
import me.hollow.trollgod.client.modules.visual.ViewmodelChanger;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderItem.class})
public class MixinRenderItem
implements Minecraftable {
    @ModifyArg(method={"renderEffect"}, at=@At(value="INVOKE", target="net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"))
    private int renderEffect(int glintVal) {
        return EnchantColor.INSTANCE.isEnabled() ? EnchantColor.INSTANCE.getColor() : glintVal;
    }

    @Inject(method={"renderItemModel"}, at={@At(value="INVOKE")})
    public void renderItem(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if (ViewmodelChanger.INSTANCE.isEnabled() && (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {
            float scale = ViewmodelChanger.INSTANCE.scale.getValue ( );
            GL11.glScalef( scale / 10.0f , scale / 10.0f , scale / 10.0f );
            float translateX = ViewmodelChanger.INSTANCE.translateX.getValue ( );
            float translateY = ViewmodelChanger.INSTANCE.translateY.getValue ( );
            float translateZ = ViewmodelChanger.INSTANCE.translateZ.getValue ( );
            float rotateX = ViewmodelChanger.INSTANCE.rotateX.getValue ( );
            float rotateY = ViewmodelChanger.INSTANCE.rotateY.getValue ( );
            float rotateZ = ViewmodelChanger.INSTANCE.rotateZ.getValue ( );
            if (transform.equals( ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND )) {
                if (MixinRenderItem.mc.player.getActiveHand() == EnumHand.OFF_HAND && MixinRenderItem.mc.player.isHandActive() && ViewmodelChanger.INSTANCE.pauseOnEat.getValue ( ) ) {
                    return;
                }
                GL11.glTranslated( translateX / 15.0f , translateY / 15.0f , translateZ / 15.0f );
                GL11.glRotatef( rotateX , 1.0f , 0.0f , 0.0f );
                GL11.glRotatef( rotateY , 0.0f , 1.0f , 0.0f );
                GL11.glRotatef( rotateZ , 0.0f , 0.0f , 1.0f );
            } else {
                if (MixinRenderItem.mc.player.getActiveHand() == EnumHand.MAIN_HAND && MixinRenderItem.mc.player.isHandActive() && ViewmodelChanger.INSTANCE.pauseOnEat.getValue ( ) ) {
                    return;
                }
                GL11.glTranslated( -translateX / 15.0f , translateY / 15.0f , translateZ / 15.0f );
                GL11.glRotatef( rotateX , 1.0f , 0.0f , 0.0f );
                GL11.glRotatef( rotateY * -1.0f , 0.0f , 1.0f , 0.0f );
                GL11.glRotatef( rotateZ * -1.0f , 0.0f , 0.0f , 1.0f );
            }
        }
    }
}

