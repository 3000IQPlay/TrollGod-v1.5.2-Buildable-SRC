/*
 * Decompiled with CFR 0.151.
 */
package me.hollow.trollgod;

import me.hollow.trollgod.client.managers.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import tcb.bces.bus.DRCEventBus;

import java.io.File;

@Mod(name="TrollGod", modid="trollgod", version="1.5.2")
public final class TrollGod {
    public static final TrollGod INSTANCE = new TrollGod();
    public static final String NAME = "TrollGod";
    public static final String VERSION = "1.5.2";
    public static final Logger logger = LogManager.getLogger( "TrollGod" );
    public static final FontManager fontManager = new FontManager();
    private final File directory;
    private final NotificationManager notificationManager;
    private final RotationManager rotationManager;
    private final CommandManager commandManager;
    private final ModuleManager moduleManager;
    private final ConfigManager configManager;
    private final FriendManager friendManager;
    private final SpeedManager speedManager;
    private final EventManager eventManager;
    private final FileManager fileManager;
    private final SafeManager safeManager;
    private final PopManager popManager;
    private final TPSManager tpsManager;
    private final DRCEventBus eventBus;

    public TrollGod() {
        this.directory = new File(Minecraft.getMinecraft().gameDir, NAME);
        this.notificationManager = new NotificationManager();
        this.rotationManager = new RotationManager();
        this.commandManager = new CommandManager();
        this.moduleManager = new ModuleManager();
        this.configManager = new ConfigManager();
        this.friendManager = new FriendManager();
        this.speedManager = new SpeedManager();
        this.eventManager = new EventManager();
        this.fileManager = new FileManager();
        this.safeManager = new SafeManager();
        this.popManager = new PopManager();
        this.tpsManager = new TPSManager();
        this.eventBus = new DRCEventBus();
    }

    public final void init() {
        this.moduleManager.init();
        this.commandManager.init();
        this.eventManager.init();
        this.friendManager.setDirectory(new File(this.directory, "friends.json"));
        this.friendManager.init();
        this.tpsManager.init();
        this.popManager.init();
        this.configManager.init();
        this.eventBus.bind();
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        Display.setTitle( "trollgod" );
    }

    public NotificationManager getNotificationManager() {
        return this.notificationManager;
    }

    public final SpeedManager getSpeedManager() {
        return this.speedManager;
    }

    public final RotationManager getRotationManager() {
        return this.rotationManager;
    }

    public final EventManager getEventManager() {
        return this.eventManager;
    }

    public final TPSManager getTpsManager() {
        return this.tpsManager;
    }

    public final DRCEventBus getBus() {
        return this.eventBus;
    }

    public final PopManager getPopManager() {
        return this.popManager;
    }

    public final FriendManager getFriendManager() {
        return this.friendManager;
    }

    public final ConfigManager getConfigManager() {
        return this.configManager;
    }

    public final ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public final SafeManager getSafeManager() {
        return this.safeManager;
    }

    static final class ShutdownThread
    extends Thread {
        ShutdownThread() {
        }

        @Override
        public void run() {
            logger.info("Trying to save config....");
            INSTANCE.getConfigManager().saveConfig(TrollGod.INSTANCE.getConfigManager().config.replaceFirst("TrollGod/", ""));
            INSTANCE.getFriendManager().unload();
            logger.info("Config saved!");
        }
    }
}

