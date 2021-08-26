package me.hollow.trollgod.client.modules.visual;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.mixin.mixins.render.AccessorRenderManager;
import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.api.util.render.RenderUtil;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

@ModuleManifest(label="Tracers", category=Module.Category.VISUAL, listen=false, color=0xFAEEAF)
public class Tracers
extends Module {
    private final Setting<Integer> yRange = this.register( new Setting <> ( "Y Range" , 50 , 0 , 255 ));
    private final AccessorRenderManager renderManager = (AccessorRenderManager)this.mc.getRenderManager();

    @Override
    public void onRender3D() {
        boolean iHateBob = this.mc.gameSettings.viewBobbing;
        this.mc.gameSettings.viewBobbing = false;
        GL11.glBlendFunc( 770 , 771 );
        GL11.glEnable( 3042 );
        GL11.glEnable( 2848 );
        GL11.glHint( 3154 , 4354 );
        GL11.glLineWidth( 1.0f );
        GL11.glDisable( 3553 );
        GL11.glDisable( 2929 );
        GL11.glDepthMask( false );
        GL11.glBegin( 1 );
        int size = this.mc.world.playerEntities.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer entity = this.mc.world.playerEntities.get(i);
            if (entity == this.mc.player || Objects.requireNonNull ( this.mc.getRenderViewEntity ( ) ).getDistance(this.mc.getRenderViewEntity().posX, entity.posY, this.mc.getRenderViewEntity().posZ) > (double) this.yRange.getValue ( ) ) continue;
            this.drawTraces( entity , this.getColorByDistance( entity ));
        }
        GL11.glEnd();
        GL11.glEnable( 3553 );
        GL11.glDisable( 2848 );
        GL11.glEnable( 2929 );
        GL11.glDepthMask( true );
        GL11.glDisable( 3042 );
        GL11.glColor4f( 1.0f , 1.0f , 1.0f , 1.0f );
        this.mc.gameSettings.viewBobbing = iHateBob;
    }

    private void drawTraces(Entity entity, Color color) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.mc.getRenderPartialTicks() - this.renderManager.getRenderPosX();
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.mc.getRenderPartialTicks() - this.renderManager.getRenderPosY();
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.mc.getRenderPartialTicks() - this.renderManager.getRenderPosZ();
        Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-((float)Math.toRadians( Objects.requireNonNull ( this.mc.getRenderViewEntity ( ) ).rotationPitch))).rotateYaw(-((float)Math.toRadians(this.mc.getRenderViewEntity().rotationYaw)));
        if (TrollGod.INSTANCE.getFriendManager().isFriend(entity.getName())) {
            RenderUtil.glColor(-11157267);
        } else {
            RenderUtil.glColor(color.getRGB());
        }
        GL11.glVertex3d( eyes.x , eyes.y + (double)this.mc.getRenderViewEntity().getEyeHeight() , eyes.z );
        GL11.glVertex3d( x , y + (double)entity.getEyeHeight() , z );
    }

    public Color getColorByDistance(Entity entity) {
        if (entity instanceof EntityPlayer && TrollGod.INSTANCE.getFriendManager().isFriend(entity.getName())) {
            return new Color(-11157267);
        }
        return new Color(Color.HSBtoRGB((float)(Math.max(0.0, Math.min(this.mc.player.getDistanceSq(entity), 2500.0) / 2500.0) / 3.0), 1.0f, 0.8f) | 0xFF000000);
    }
}

