package me.hollow.trollgod.client.modules.visual;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.mixin.mixins.render.AccessorRenderManager;
import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.api.util.EntityUtil;
import me.hollow.trollgod.api.util.render.RenderUtil;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import me.hollow.trollgod.client.modules.client.Colours;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;
import java.util.Objects;

@ModuleManifest(label="Nametags", listen=false, category=Module.Category.VISUAL, color=0xFF0066)
public final class Nametags
extends Module {
    private final Setting<Boolean> armor = this.register( new Setting <> ( "Armor" , true ));
    private final Setting<Float> scaling = this.register( new Setting <> ( "Size" , 0.3f , 0.1f , 20.0f ));
    private final Setting<Boolean> ping = this.register( new Setting <> ( "Ping" , true ));
    private final Setting<Boolean> totemPops = this.register( new Setting <> ( "TotemPops" , true ));
    private final Setting<Boolean> rect = this.register( new Setting <> ( "Rectangle" , true ));
    private final Setting<Boolean> rectBorder = this.register( new Setting <> ( "Border" , true , v -> this.rect.getValue ( ) ));
    private final Setting<Boolean> gradientBorder = this.register( new Setting <> ( "GradientBorder" , false , v -> this.rectBorder.getValue ( ) ));
    private final Setting<Boolean> sneak = this.register( new Setting <> ( "SneakColor" , false ));
    private final Setting<Boolean> scaleing = this.register( new Setting <> ( "Scale" , false ));
    private final Setting<Float> factor = this.register( new Setting <> ( "Factor" , 0.3f , 0.1f , 1.0f , v -> this.scaleing.getValue ( ) ));
    public static Nametags INSTANCE;
    private final ICamera camera = new Frustum();
    private final AccessorRenderManager renderManager = (AccessorRenderManager)this.mc.getRenderManager();

    public Nametags() {
        INSTANCE = this;
    }

    @Override
    public final void onRender3D() {
        if (this.mc.getRenderViewEntity() == null) {
            return;
        }
        this.camera.setPosition(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY, this.mc.getRenderViewEntity().posZ);
        int size = this.mc.world.playerEntities.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer player = this.mc.world.playerEntities.get(i);
            if (!this.camera.isBoundingBoxInFrustum(player.getEntityBoundingBox()) || player == this.mc.player || player.isDead || !(player.getHealth() > 0.0f)) continue;
            this.renderNameTag(player, this.interpolate(player.lastTickPosX, player.posX, this.mc.getRenderPartialTicks()) - this.renderManager.getRenderPosX(), this.interpolate(player.lastTickPosY, player.posY, this.mc.getRenderPartialTicks()) - this.renderManager.getRenderPosY(), this.interpolate(player.lastTickPosZ, player.posZ, this.mc.getRenderPartialTicks()) - this.renderManager.getRenderPosZ(), this.mc.getRenderPartialTicks());
        }
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += player.isSneaking() ? 0.5 : 0.7;
        Entity camera = this.mc.getRenderViewEntity();
        double originalPositionX = Objects.requireNonNull ( camera ).posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = this.getDisplayTag(player);
        double distance = camera.getDistance(x + this.mc.getRenderManager().viewerPosX, y + this.mc.getRenderManager().viewerPosY, z + this.mc.getRenderManager().viewerPosZ);
        int width = this.mc.fontRenderer.getStringWidth(displayTag) >> 1;
        double scale = (0.0018 + (double) this.scaling.getValue ( ) * (distance * (double) this.factor.getValue ( ) )) / 1000.0;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        if (! this.scaleing.getValue ( ) ) {
            scale = (double) this.scaling.getValue ( ) / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset( 1.0f , -1500000.0f );
        GlStateManager.disableLighting();
        GlStateManager.translate( (float)x , (float)tempY + 1.4f , (float)z );
        GlStateManager.rotate( -this.mc.getRenderManager().playerViewY , 0.0f , 1.0f , 0.0f );
        GlStateManager.rotate( this.mc.getRenderManager().playerViewX , this.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f , 0.0f , 0.0f );
        GlStateManager.scale( -scale , -scale , scale );
        GlStateManager.disableDepth();
        if ( this.rect.getValue ( ) ) {
            if ( this.gradientBorder.getValue ( ) ) {
                RenderUtil.drawGradientBorderedRect(-width - 2, -(this.mc.fontRenderer.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, 0x55000000);
            } else {
                RenderUtil.drawBorderedRect(-width - 2, -(this.mc.fontRenderer.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, 0x55000000, this.rectBorder.getValue ( ) ? (TrollGod.INSTANCE.getFriendManager().isFriend(player) ? -11157267 : Colours.INSTANCE.getColor()) : 0);
            }
        }
        if ( this.armor.getValue ( ) ) {
            GlStateManager.pushMatrix();
            int xOffset = -8;
            for (int i = 0; i < 4; ++i) {
                xOffset -= 8;
            }
            ItemStack renderOffhand = player.getHeldItemOffhand().copy();
            this.renderItemStack(renderOffhand, xOffset -= 8);
            xOffset += 16;
            for (int i = 0; i < 4; ++i) {
                this.renderItemStack( player.inventory.armorInventory.get(i).copy(), xOffset);
                xOffset += 16;
            }
            this.renderItemStack(player.getHeldItemMainhand().copy(), xOffset);
            GlStateManager.popMatrix();
        }
        this.mc.fontRenderer.drawStringWithShadow(displayTag, (float)(-width), -8.0f, this.getDisplayColour(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset( 1.0f , 1500000.0f );
        GlStateManager.popMatrix();
    }

    private void renderItemStack(ItemStack stack, int x) {
        GlStateManager.depthMask( true );
        GlStateManager.clear( 256 );
        RenderHelper.enableStandardItemLighting();
        this.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, -26);
        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, stack, x, -26);
        this.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale( 0.5f , 0.5f , 0.5f );
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x, -26);
        GlStateManager.enableDepth();
        GlStateManager.scale( 2.0f , 2.0f , 2.0f );
    }

    private void renderEnchantmentText(ItemStack stack, int x, int y) {
        int enchantmentY = y - 8;
        NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            short id = enchants.getCompoundTagAt(index).getShort("id");
            short level = enchants.getCompoundTagAt(index).getShort("lvl");
            Enchantment enc = Enchantment.getEnchantmentByID( id );
            if (enc == null || enc.getName().contains("fall") || !enc.getName().contains("all") && !enc.getName().contains("explosion")) continue;
            this.mc.fontRenderer.drawStringWithShadow(enc.isCurse() ? TextFormatting.RED + enc.getTranslatedName( level ).substring(11).substring(0, 1).toLowerCase() : enc.getTranslatedName( level ).substring(0, 1).toLowerCase() + level, (float)(x * 2), (float)enchantmentY, -1);
            enchantmentY -= 8;
        }
        if (Nametags.hasDurability(stack)) {
            int percent = Nametags.getRoundedDamage(stack);
            String color = percent >= 60 ? ChatFormatting.GREEN.toString() : (percent >= 25 ? ChatFormatting.YELLOW.toString() : ChatFormatting.RED.toString());
            this.mc.fontRenderer.drawStringWithShadow(color + percent + "%", (float)(x << 1), (float)enchantmentY, -1);
        }
    }

    public static int getItemDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }

    public static float getDamageInPercent(ItemStack stack) {
        return (float)Nametags.getItemDamage(stack) / (float)stack.getMaxDamage() * 100.0f;
    }

    public static int getRoundedDamage(ItemStack stack) {
        return (int)Nametags.getDamageInPercent(stack);
    }

    public static boolean hasDurability(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }

    private String getDisplayTag(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        float health = EntityUtil.getHealth( player );
        String color = health > 18.0f ? ChatFormatting.GREEN.toString() : (health > 16.0f ? ChatFormatting.DARK_GREEN.toString() : (health > 12.0f ? ChatFormatting.YELLOW.toString() : (health > 8.0f ? ChatFormatting.GOLD.toString() : (health > 5.0f ? ChatFormatting.RED.toString() : ChatFormatting.DARK_RED.toString()))));
        String pingStr = "";
        if ( this.ping.getValue ( ) ) {
            try {
                int responseTime = Objects.requireNonNull(this.mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                pingStr = pingStr + responseTime + "ms";
            }
            catch (Exception ignored) {
                pingStr = pingStr + "-1ms";
            }
        }
        String popStr = "";
        if ( this.totemPops.getValue ( ) ) {
            Map<String, Integer> registry = TrollGod.INSTANCE.getPopManager().getPopMap();
            popStr = popStr + (registry.containsKey(player.getName()) ? " -" + registry.get(player.getName()) : "");
        }
        name = name + color + " " + (int)health;
        return name + " " + ChatFormatting.RESET + pingStr + popStr;
    }

    private int getDisplayColour(EntityPlayer player) {
        if (TrollGod.INSTANCE.getFriendManager().isFriend(player)) {
            return -11157267;
        }
        if (player.isSneaking() && this.sneak.getValue ( ) ) {
            return 16757248;
        }
        return -1;
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }
}

