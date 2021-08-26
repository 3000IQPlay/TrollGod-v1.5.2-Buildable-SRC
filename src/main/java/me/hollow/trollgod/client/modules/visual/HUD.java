/*
 * Decompiled with CFR 0.151.
 */
package me.hollow.trollgod.client.modules.visual;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.api.util.ItemUtil;
import me.hollow.trollgod.api.util.font.RainbowText;
import me.hollow.trollgod.client.events.ClientEvent;
import me.hollow.trollgod.client.events.UpdateEvent;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import me.hollow.trollgod.client.modules.client.Colours;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import tcb.bces.listener.Subscribe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@ModuleManifest(label="HUD", category=Module.Category.CLIENT, color=39423)
public final class HUD
extends Module {
    public static Setting<Boolean> rainbowSetting = new Setting <> ( "Rainbow" , true );
    public static Setting<Integer> speed = new Setting <> ( "Speed" , 5000 , 1000 , 15000 );
    public static Setting<Integer> saturation = new Setting <> ( "Saturation" , 200 , 0 , 255 );
    public static Setting<Integer> brightness = new Setting <> ( "Brightness" , 255 , 0 , 255 );
    public static Setting<Integer> factor = new Setting <> ( "Hue" , 5 , 0 , 40 );
    private final Setting<Boolean> arrayList = this.register( new Setting <> ( "Arraylist" , true ));
    private final Setting<SortMode> sortMode = this.register( new Setting <> ( "Sorting" , SortMode.LENGTH ));
    private final Setting<Boolean> renderWatermark = this.register( new Setting <> ( "Watermark" , true ));
    private final Setting<Boolean> offsetWatermark = this.register(new Setting<Object>("OffsetWatermark", false, v -> this.renderWatermark.getValue()));
    private final Setting<Boolean> customWatermark = this.register(new Setting<Object>("Custom", false, v -> this.renderWatermark.getValue()));
    private final Setting<String> watermarkString = this.register(new Setting<Object>("CustomMark", "StresserLegend.CC v573.5", v -> this.customWatermark.getValue()));
    private final Setting<Boolean> armorHud = this.register( new Setting <> ( "Armor" , true ));
    private final Setting<Boolean> renderArmor = this.register( new Setting <> ( "Render Armor" , true , v -> this.armorHud.getValue ( ) ));
    private final Setting<Boolean> tps = this.register( new Setting <> ( "TPS" , true ));
    private final Setting<Boolean> coords = this.register( new Setting <> ( "Coordinates" , true ));
    private final Setting<Boolean> ping = this.register( new Setting <> ( "Ping" , true ));
    private final Setting<Boolean> direction = this.register( new Setting <> ( "Direction" , true ));
    private final Setting<Boolean> welcomer = this.register( new Setting <> ( "Welcomer" , true ));
    private final Setting<Boolean> kmh = this.register( new Setting <> ( "KMH" , true ));
    private final Setting<Boolean> fps = this.register( new Setting <> ( "FPS" , true ));
    private List<Module> modules;
    public static HUD INSTANCE;
    private int size = 0;

    public HUD() {
        INSTANCE = this;
        this.addSetting(rainbowSetting, saturation, brightness, speed, factor);
    }

    @Override
    public void onLoad() {
        this.modules = new ArrayList <> ( TrollGod.INSTANCE.getModuleManager ( ).getModules ( ) );
        this.size = this.modules.size();
    }

    @Subscribe
    public void onSettingChange(ClientEvent event) {
        if (event.getSetting() == this.sortMode) {
            this.modules.sort(Comparator.comparing(Module::getLabel));
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.sortMode.getValue() == SortMode.LENGTH) {
            if (this.mc.player.ticksExisted % 2 != 0) {
                return;
            }
            this.modules.sort(Comparator.comparingDouble(mod -> -this.getStringWidth(mod.getLabel() + mod.getSuffix())));
        }
    }

    public void drawString(String text, float x, float y, int color) {
        if ( rainbowSetting.getValue ( ) ) {
            RainbowText.drawStringWithShadow(text, x, y, HUD.rainbow(x, y), false);
        } else {
            TrollGod.fontManager.drawString(text, x, y, color);
        }
    }

    private static int rainbow(double x, double y) {
        double scale = 0.01568627450980392;
        double d = 0.7071067811865483;
        double pos = (x * d + y * d) * -scale + (double)(System.currentTimeMillis() % (long) speed.getValue ( ) ) * 2.0 * -Math.PI / (double) speed.getValue ( );
        return 0xFF000000 | MathHelper.clamp( MathHelper.floor( 255.0 * (0.5 + Math.sin(0.0 + pos)) ) , 0 , 255 ) << 16 | MathHelper.clamp( MathHelper.floor( 255.0 * (0.5 + Math.sin(2.0943951023931953 + pos)) ) , 0 , 255 ) << 8 | MathHelper.clamp( MathHelper.floor( 255.0 * (0.5 + Math.sin(4.1887902047863905 + pos)) ) , 0 , 255 );
    }

    public float getStringWidth(String text) {
        if ( rainbowSetting.getValue ( ) ) {
            return RainbowText.rainbow.getStringWidth(text);
        }
        return TrollGod.fontManager.getStringWidth(text);
    }

    public final void onRender2D() {
        int daFunnies;
        if (!this.isEnabled()) {
            return;
        }
        ScaledResolution resolution = new ScaledResolution(this.mc);
        if ( this.renderWatermark.getValue ( ) ) {
            this.drawString( this.customWatermark.getValue ( ) ? this.watermarkString.getValue() : "TrollGod.CC \u00a77v1.5.2", 2.0f, this.offsetWatermark.getValue ( ) ? 10.0f : 2.0f, Colours.INSTANCE.getColor());
        }
        if ( this.armorHud.getValue ( ) ) {
            GlStateManager.enableTexture2D();
            int i = resolution.getScaledWidth() >> 1;
            int y = resolution.getScaledHeight() - 55 - (this.mc.player.isInWater() && this.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
            for (int j = 0; j < 4; ++j) {
                ItemStack is = this.mc.player.inventory.armorInventory.get(j);
                if (is.isEmpty()) continue;
                int x = i - 90 + (9 - j - 1) * 20 + 2;
                if ( this.renderArmor.getValue ( ) ) {
                    GlStateManager.enableDepth();
                    this.mc.getRenderItem().zLevel = 200.0f;
                    this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
                    this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRenderer, is, x, y + ( this.renderArmor.getValue ( ) ? 0 : 10), "");
                    this.mc.getRenderItem().zLevel = 0.0f;
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                }
                int dmg = (int)ItemUtil.getDamageInPercent(is);
                TrollGod.fontManager.drawString(dmg + "", x + 8 - (this.mc.fontRenderer.getStringWidth(dmg + "") >> 1), y + ( this.renderArmor.getValue ( ) ? -8 : 6), is.getItem().getRGBDurabilityForDisplay(is));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        if ( this.coords.getValue ( ) ) {
            this.drawString("XYZ \u00a7f" + (int)this.mc.player.posX + ", " + (int)this.mc.player.posY + ", " + (int)this.mc.player.posZ, 2.0f, resolution.getScaledHeight() - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 22 : 10), Colours.INSTANCE.getColor());
        }
        if ( this.welcomer.getValue ( ) ) {
            String welcomerString = "Welcome, " + this.mc.player.getName();
            this.drawString(welcomerString, (float)resolution.getScaledWidth() / 2.0f - this.getStringWidth(welcomerString) / 2.0f, 2.0f, Colours.INSTANCE.getColor());
        }
        boolean inChat = this.mc.ingameGUI.getChatGUI().getChatOpen();
        if ( this.direction.getValue ( ) ) {
            String facing = "";
            switch (Objects.requireNonNull ( this.mc.getRenderViewEntity ( ) ).getHorizontalFacing()) {
                case NORTH: {
                    facing = "North \u00a78[\u00a7r-Z\u00a78]";
                    break;
                }
                case SOUTH: {
                    facing = "South \u00a78[\u00a7r+Z\u00a78]";
                    break;
                }
                case WEST: {
                    facing = "West \u00a78[\u00a7r-X\u00a78]";
                    break;
                }
                case EAST: {
                    facing = "East \u00a78[\u00a7r+X\u00a78]";
                }
            }
            this.drawString(facing, 2.0f, resolution.getScaledHeight() - (inChat ? 24 : 10) - ( this.coords.getValue ( ) ? 10 : 0), Colours.INSTANCE.getColor());
        }
        int n = daFunnies = inChat ? 14 : 0;
        if ( this.kmh.getValue ( ) ) {
            String kmhString = "Speed \u00a7f" + TrollGod.INSTANCE.getSpeedManager().getKpH() + "km/h";
            this.drawString(kmhString, (float)resolution.getScaledWidth() - this.getStringWidth(kmhString) - 2.0f, resolution.getScaledHeight() - (inChat ? 24 : 10), Colours.INSTANCE.getColor());
            daFunnies += 10;
        }
        if ( this.tps.getValue ( ) ) {
            String tpsString = "TPS \u00a7f" + String.format("%.1f", TrollGod.INSTANCE.getTpsManager ( ).getTPS ( ) );
            this.drawString(tpsString, (float)resolution.getScaledWidth() - this.getStringWidth(tpsString) - 2.0f, resolution.getScaledHeight() - 10 - daFunnies, Colours.INSTANCE.getColor());
            daFunnies += 10;
        }
        if ( this.fps.getValue ( ) ) {
            String fpsString = "FPS \u00a7f" + Minecraft.getDebugFPS();
            this.drawString(fpsString, (float)resolution.getScaledWidth() - this.getStringWidth(fpsString) - 2.0f, resolution.getScaledHeight() - 10 - daFunnies, Colours.INSTANCE.getColor());
            daFunnies += 10;
        }
        if ( this.ping.getValue ( ) ) {
            String pingString = "Ping \u00a7f" + TrollGod.INSTANCE.getTpsManager().getPing();
            this.drawString(pingString, (float)resolution.getScaledWidth() - this.getStringWidth(pingString) - 2.0f, resolution.getScaledHeight() - 10 - daFunnies, Colours.INSTANCE.getColor());
        }
        if ( this.arrayList.getValue ( ) ) {
            int offset = -6;
            for (int i = 0; i < this.size; ++i) {
                Module module = this.modules.get(i);
                if (!module.isEnabled() || module.isHidden()) continue;
                String fullLabel = module.getLabel() + module.getSuffix();
                this.drawString(fullLabel, (float)resolution.getScaledWidth() - this.getStringWidth(fullLabel) - 2.0f, offset += 10, module.getColor());
            }
        }
    }

    public
    enum SortMode {
        ALPHABETICAL,
        LENGTH

    }
}

