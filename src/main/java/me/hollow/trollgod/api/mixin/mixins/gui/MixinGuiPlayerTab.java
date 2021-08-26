package me.hollow.trollgod.api.mixin.mixins.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.client.modules.client.Manage;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiPlayerTabOverlay.class})
public class MixinGuiPlayerTab {
    @Inject(method={"getPlayerName"}, at={@At(value="HEAD")}, cancellable=true)
    public void getPlayerName( NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable < java.io.Serializable > returnable) {
        if ( Manage.INSTANCE.tabTweaks.getValue ( ) ) {
            returnable.cancel();
            returnable.setReturnValue(this.getPlayerName(networkPlayerInfoIn));
        }
    }

    public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String dname;
        String string = dname = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName( networkPlayerInfoIn.getPlayerTeam() , networkPlayerInfoIn.getGameProfile().getName() );
        if (TrollGod.INSTANCE.getFriendManager().isFriend(dname)) {
            String tweaks = "";
            if ( Manage.INSTANCE.highlightFriends.getValue ( ) ) {
                tweaks = tweaks + ChatFormatting.DARK_PURPLE + "" + ChatFormatting.BOLD;
            }
            return tweaks + dname;
        }
        return dname;
    }
}

