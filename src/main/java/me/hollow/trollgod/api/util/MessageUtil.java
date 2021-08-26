package me.hollow.trollgod.api.util;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil
implements Minecraftable {
    private static final String prefix = "[\u00a75TrollGod\u00a7r] \u00a7d";

    public static void sendClientMessage(String string, boolean deleteOld) {
        if (MessageUtil.mc.player == null) {
            return;
        }
        TextComponentString component = new TextComponentString(prefix + string);
        if (deleteOld) {
            MessageUtil.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion( component , -727);
        } else {
            MessageUtil.mc.ingameGUI.getChatGUI().printChatMessage( component );
        }
    }

    public static void sendClientMessage(String string, int id) {
        if (MessageUtil.mc.player == null) {
            return;
        }
        TextComponentString component = new TextComponentString(prefix + string);
        MessageUtil.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion( component , id);
    }
}

