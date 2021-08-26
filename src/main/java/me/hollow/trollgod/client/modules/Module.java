package me.hollow.trollgod.client.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.property.Bind;
import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.api.util.MessageUtil;
import me.hollow.trollgod.client.gui.TrollGui;
import net.minecraft.client.Minecraft;
import tcb.bces.listener.IListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module
implements IListener {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private final List<Setting> settings = new ArrayList <> ( );
    public final Setting<Boolean> drawn = this.register( new Setting <> ( "Drawn" , true ));
    public final Setting<Bind> bind = this.register( new Setting <> ( "Bind" , new Bind ( - 10000 ) ));
    private String label;
    private String suffix = "";
    private Category category;
    private boolean persistent;
    private boolean enabled;
    private int color;

    public Module() {
        if (this.getClass().isAnnotationPresent(ModuleManifest.class)) {
            ModuleManifest moduleManifest = this.getClass().getAnnotation(ModuleManifest.class);
            this.label = moduleManifest.label();
            this.category = moduleManifest.category();
            this.bind.setValue(new Bind(moduleManifest.key()));
            this.drawn.setValue(moduleManifest.drawn());
            this.persistent = moduleManifest.persistent();
            if (this.persistent) {
                this.enabled = true;
            }
            this.color = moduleManifest.color();
            if (moduleManifest.listen()) {
                TrollGod.INSTANCE.getBus().register(this);
            }
        }
    }

    public void addSetting(Setting ... setting) {
        this.settings.addAll(Arrays.asList(setting));
    }

    public final Setting register(Setting setting) {
        this.settings.add(setting);
        if (this.mc.currentScreen instanceof TrollGui) {
            TrollGui.getInstance().updateModule(this);
        }
        return setting;
    }

    public final List<Setting> getSettings() {
        return this.settings;
    }

    public final void setEnabled(boolean enabled) {
        if (this.persistent) {
            this.enabled = true;
            return;
        }
        this.enabled = enabled;
        this.onToggle();
        if (enabled) {
            this.onEnable();
            MessageUtil.sendClientMessage(ChatFormatting.DARK_AQUA + this.getLabel() + "\u00a7d was \u00a72enabled", -44444);
        } else {
            this.onDisable();
            MessageUtil.sendClientMessage(ChatFormatting.DARK_AQUA + this.getLabel() + "\u00a7d was \u00a74disabled", -44444);
        }
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    public void disable() {
        this.setEnabled(false);
    }

    public void onRender3D() {
    }

    public void onUpdate() {
    }

    public void onToggle() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onLoad() {
    }

    public void onDisconnect() {
    }

    public final boolean isNull() {
        return this.mc.player == null || this.mc.world == null;
    }

    public final int getKey() {
        return this.bind.getValue().getKey();
    }

    public final boolean isEnabled() {
        return this.enabled;
    }

    public final boolean isHidden() {
        return ! this.drawn.getValue ( );
    }

    public final boolean isPersistent() {
        return this.persistent;
    }

    public int getColor() {
        return this.color;
    }

    public final Category getCategory() {
        return this.category;
    }

    public final String getLabel() {
        return this.label;
    }

    public final void clearSuffix() {
        this.suffix = "";
    }

    public final void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public final String getSuffix() {
        if (this.suffix.length() == 0) {
            return "";
        }
        return " \u00a78[\u00a7f" + this.suffix + "\u00a78]";
    }

    @Override
    public final boolean isListening() {
        return this.enabled && this.mc.player != null;
    }

    public
    enum Category {
        COMBAT,
        MOVEMENT,
        PLAYER,
        CLIENT,
        VISUAL,
        MISC

    }
}

