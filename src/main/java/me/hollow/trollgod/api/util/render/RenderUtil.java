package me.hollow.trollgod.api.util.render;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import me.hollow.trollgod.api.mixin.mixins.render.AccessorRenderManager;
import me.hollow.trollgod.api.util.ColorUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

public final class RenderUtil
implements Minecraftable {
    public static final ICamera camera = new Frustum();
    public static final Tessellator tessellator = Tessellator.getInstance();
    private static boolean depth = GL11.glIsEnabled( 2896 );
    private static boolean texture = GL11.glIsEnabled( 3042 );
    private static boolean clean = GL11.glIsEnabled( 3553 );
    private static boolean bind = GL11.glIsEnabled( 2929 );
    private static boolean override = GL11.glIsEnabled( 2848 );
    static final AccessorRenderManager renderManager = (AccessorRenderManager)mc.getRenderManager();

    public static void GlPost() {
        RenderUtil.GLPost(depth, texture, clean, bind, override);
    }

    private static void GLPost(boolean depth, boolean texture, boolean clean, boolean bind, boolean override) {
        GlStateManager.depthMask( true );
        if (!override) {
            GL11.glDisable( 2848 );
        }
        if (bind) {
            GL11.glEnable( 2929 );
        }
        if (clean) {
            GL11.glEnable( 3553 );
        }
        if (!texture) {
            GL11.glDisable( 3042 );
        }
        if (depth) {
            GL11.glEnable( 2896 );
        }
    }

    public static void GLPre(float lineWidth) {
        depth = GL11.glIsEnabled( 2896 );
        texture = GL11.glIsEnabled( 3042 );
        clean = GL11.glIsEnabled( 3553 );
        bind = GL11.glIsEnabled( 2929 );
        override = GL11.glIsEnabled( 2848 );
        RenderUtil.GLPre(depth, texture, clean, bind, override, lineWidth);
    }

    private static void GLPre(boolean depth, boolean texture, boolean clean, boolean bind, boolean override, float lineWidth) {
        if (depth) {
            GL11.glDisable( 2896 );
        }
        if (!texture) {
            GL11.glEnable( 3042 );
        }
        GL11.glLineWidth( lineWidth );
        if (clean) {
            GL11.glDisable( 3553 );
        }
        if (bind) {
            GL11.glDisable( 2929 );
        }
        if (!override) {
            GL11.glEnable( 2848 );
        }
        GlStateManager.blendFunc( GlStateManager.SourceFactor.SRC_ALPHA , GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA );
        GL11.glHint( 3154 , 4354 );
        GlStateManager.depthMask( false );
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;
        GlStateManager.translate( (double)x - renderManager.getRenderPosX() , (double)y - renderManager.getRenderPosY() , (double)z - renderManager.getRenderPosZ() );
        GlStateManager.glNormal3f( 0.0f , 1.0f , 0.0f );
        GlStateManager.rotate( -Minecraft.getMinecraft().player.rotationYaw , 0.0f , 1.0f , 0.0f );
        GlStateManager.rotate( Minecraft.getMinecraft().player.rotationPitch , Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f , 0.0f , 0.0f );
        GlStateManager.scale( -scale , -scale , scale );
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        RenderUtil.glBillboard(x, y, z);
        int distance = (int)player.getDistance( x , y , z );
        float scaleDistance = (float)(distance >> 1) / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale( scaleDistance , scaleDistance , scaleDistance );
    }

    public static double getDiff(double lastI, double i, float ticks, double ownI) {
        return lastI + (i - lastI) * (double)ticks - ownI;
    }

    public static void drawLine(float x, float y, float x1, float y1, float width) {
        GL11.glDisable( 3553 );
        GL11.glLineWidth( width );
        GL11.glBegin( 1 );
        GL11.glVertex2f( x , y );
        GL11.glVertex2f( x1 , y1 );
        GL11.glEnd();
        GL11.glEnable( 3553 );
    }

    public static void drawRect(Rectangle rectangle, int color) {
        RenderUtil.drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, color);
    }

    public static void drawRect(float x, float y, float x1, float y1, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA , GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA , GlStateManager.SourceFactor.ONE , GlStateManager.DestFactor.ZERO );
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos( x , y1 , 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos( x1 , y1 , 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos( x1 , y , 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos( x , y , 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION);
        builder.pos( x , y1 , 0.0).endVertex();
        builder.pos( x1 , y1 , 0.0).endVertex();
        builder.pos( x1 , y , 0.0).endVertex();
        builder.pos( x , y , 0.0).endVertex();
        tessellator.draw();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        RenderUtil.enableGL2D();
        GL11.glScalef( 0.5f , 0.5f , 0.5f );
        RenderUtil.drawVLine(x *= 2.0f, y *= 2.0f, (y1 *= 2.0f) - 1.0f, borderC);
        RenderUtil.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
        RenderUtil.drawHLine(x, x1 - 1.0f, y, borderC);
        RenderUtil.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        RenderUtil.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef( 2.0f , 2.0f , 2.0f );
        RenderUtil.disableGL2D();
    }

    public static void drawOutlineRect(float x, float y, float w, float h, float lineWidth, int c) {
        RenderUtil.drawRect(x, y, x - lineWidth, h, c);
        RenderUtil.drawRect(w + lineWidth, y, w, h, c);
        RenderUtil.drawRect(x, y, w, y - lineWidth, c);
        RenderUtil.drawRect(x, h + lineWidth, w, h, c);
    }

    public static void disableGL2D(boolean unused) {
        GL11.glEnable( 3553 );
        GL11.glDisable( 3042 );
        GL11.glEnable( 2929 );
        GL11.glDisable( 2848 );
        GL11.glHint( 3154 , 4352 );
        GL11.glHint( 3155 , 4352 );
    }

    public static void enableGL2D(boolean unused) {
        GL11.glDisable( 2929 );
        GL11.glEnable( 3042 );
        GL11.glDisable( 3553 );
        GL11.glBlendFunc( 770 , 771 );
        GL11.glDepthMask( true );
        GL11.glEnable( 2848 );
        GL11.glHint( 3154 , 4354 );
        GL11.glHint( 3155 , 4354 );
    }

    public static void drawRect(float x, float y, float x1, float y1, int color, int ignored) {
        RenderUtil.enableGL2D(false);
        RenderUtil.glColor(color);
        GL11.glBegin( 7 );
        GL11.glVertex2f( x , y1 );
        GL11.glVertex2f( x1 , y1 );
        GL11.glVertex2f( x1 , y );
        GL11.glVertex2f( x , y );
        GL11.glEnd();
        RenderUtil.disableGL2D(false);
    }

    public static void enableGL2D() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA , GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA , GlStateManager.SourceFactor.ONE , GlStateManager.DestFactor.ZERO );
    }

    public static void disableGL2D() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientBorderedRect(float x, float y, float x1, float y1, int insideC) {
        RenderUtil.enableGL2D();
        GL11.glScalef( 0.5f , 0.5f , 0.5f );
        RenderUtil.drawVLine(x *= 2.0f, y *= 2.0f, (y1 *= 2.0f) - 1.0f, new Color(ColorUtil.getRainbow(5000, 0, 1.0f)).getRGB());
        RenderUtil.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, new Color(ColorUtil.getRainbow(5000, 1000, 1.0f)).getRGB());
        RenderUtil.drawGradientHLine(x, x1 - 1.0f, y, new Color(ColorUtil.getRainbow(5000, 0, 1.0f)).getRGB(), new Color(ColorUtil.getRainbow(5000, 1000, 1.0f)).getRGB());
        RenderUtil.drawGradientHLine(x, x1 - 2.0f, y1 - 1.0f, new Color(ColorUtil.getRainbow(5000, 0, 1.0f)).getRGB(), new Color(ColorUtil.getRainbow(5000, 1000, 1.0f)).getRGB());
        RenderUtil.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef( 2.0f , 2.0f , 2.0f );
        RenderUtil.disableGL2D();
    }

    public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        float alpha = (float)(topColor >> 24 & 0xFF) / 255.0f;
        float red = (float)(topColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(topColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(topColor & 0xFF) / 255.0f;
        float alpha2 = (float)(bottomColor >> 24 & 0xFF) / 255.0f;
        float red2 = (float)(bottomColor >> 16 & 0xFF) / 255.0f;
        float green2 = (float)(bottomColor >> 8 & 0xFF) / 255.0f;
        float blue2 = (float)(bottomColor & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA , GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA , GlStateManager.SourceFactor.ONE , GlStateManager.DestFactor.ZERO );
        GlStateManager.shadeModel( 7425 );
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos( x , y , 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos( x , y1 , 0.0).color(red, green, blue, alpha).endVertex();
        builder.pos( x1 , y1 , 0.0).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos( x1 , y , 0.0).color(red2, green2, blue2, alpha2).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel( 7424 );
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void drawProperOutline(BlockPos pos, Color color) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Entity player = mc.getRenderViewEntity();
        double d3 = Objects.requireNonNull ( player ).lastTickPosX + (player.posX - player.lastTickPosX) * (double)mc.getRenderPartialTicks();
        double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)mc.getRenderPartialTicks();
        double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)mc.getRenderPartialTicks();
        RenderGlobal.drawSelectionBoundingBox( iblockstate.getSelectedBoundingBox( RenderUtil.mc.world , pos).grow( 0.002f ).offset(-d3, -d4, -d5) , (float)color.getRed() / 255.0f , (float)color.getGreen() / 255.0f , (float)color.getBlue() / 255.0f , (float)color.getAlpha() / 255.0f );
    }

    public static void renderProperOutline(BlockPos pos, Color color, float lineWidth) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate( 770 , 771 , 0 , 1 );
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask( false );
        GL11.glEnable( 2848 );
        GL11.glHint( 3154 , 4354 );
        GL11.glLineWidth( lineWidth );
        RenderUtil.drawProperOutline(pos, color);
        GL11.glDisable( 2848 );
        GlStateManager.depthMask( true );
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawProperBox(BlockPos pos, Color color, int alpha) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Entity player = mc.getRenderViewEntity();
        double d3 = Objects.requireNonNull ( player ).lastTickPosX + (player.posX - player.lastTickPosX) * (double)mc.getRenderPartialTicks();
        double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)mc.getRenderPartialTicks();
        double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)mc.getRenderPartialTicks();
        RenderGlobal.renderFilledBox( iblockstate.getSelectedBoundingBox( RenderUtil.mc.world , pos).grow(0.002).offset(-d3, -d4, -d5) , (float)color.getRed() / 255.0f , (float)color.getGreen() / 255.0f , (float)color.getBlue() / 255.0f , (float)alpha / 255.0f );
    }

    public static void drawBoundingBox(AxisAlignedBB bb, Color color, float lineWidth) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate( 770 , 771 , 0 , 1 );
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask( false );
        GL11.glEnable( 2848 );
        GL11.glHint( 3154 , 4354 );
        GL11.glLineWidth( lineWidth );
        RenderGlobal.drawBoundingBox( bb.minX , bb.minY , bb.minZ , bb.maxX , bb.maxY , bb.maxZ , (float)color.getRed() / 255.0f , (float)color.getGreen() / 255.0f , (float)color.getBlue() / 255.0f , (float)color.getAlpha() / 255.0f );
        GL11.glDisable( 2848 );
        GlStateManager.depthMask( true );
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawProperBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha, float height) {
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate( 770 , 771 , 0 , 1 );
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask( false );
            GL11.glEnable( 2848 );
            GL11.glHint( 3154 , 4354 );
            GL11.glLineWidth( lineWidth );
            if (box) {
                RenderUtil.drawProperBox(pos, color, boxAlpha);
            }
            if (outline) {
                RenderUtil.drawProperOutline(pos, color);
            }
            GL11.glDisable( 2848 );
            GlStateManager.depthMask( true );
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha, float height) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)((float)pos.getY() + height) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate( 770 , 771 , 0 , 1 );
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask( false );
            GL11.glEnable( 2848 );
            GL11.glHint( 3154 , 4354 );
            GL11.glLineWidth( lineWidth );
            if (box) {
                RenderGlobal.renderFilledBox( bb , (float)color.getRed() / 255.0f , (float)color.getGreen() / 255.0f , (float)color.getBlue() / 255.0f , (float)boxAlpha / 255.0f );
            }
            if (outline) {
                RenderGlobal.drawBoundingBox( bb.minX , bb.minY , bb.minZ , bb.maxX , bb.maxY , bb.maxZ , (float)color.getRed() / 255.0f , (float)color.getGreen() / 255.0f , (float)color.getBlue() / 255.0f , (float)color.getAlpha() / 255.0f );
            }
            GL11.glDisable( 2848 );
            GlStateManager.depthMask( true );
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void renderCrosses(BlockPos pos, Color color, float lineWidth) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(pos))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate( 770 , 771 , 0 , 1 );
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask( false );
            GL11.glEnable( 2848 );
            GL11.glHint( 3154 , 4354 );
            GL11.glLineWidth( lineWidth );
            RenderUtil.renderCrosses(bb, color);
            GL11.glDisable( 2848 );
            GlStateManager.depthMask( true );
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderUtil.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawGradientHLine(float x, float y, float x1, int color1, int color2) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderUtil.drawGradientHRect(x, x1, y + 1.0f, x1 + 1.0f, color1, color2);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        RenderUtil.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f( red , green , blue , alpha );
    }

    public static void renderCrosses(AxisAlignedBB bb, Color color) {
        int hex = color.getRGB();
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, 1.0f).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, 1.0f).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, 1.0f).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, 1.0f).endVertex();
        tessellator.draw();
    }
}

