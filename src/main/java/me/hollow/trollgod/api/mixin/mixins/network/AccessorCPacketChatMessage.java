package me.hollow.trollgod.api.mixin.mixins.network;

import net.minecraft.network.play.client.CPacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={CPacketChatMessage.class})
public interface AccessorCPacketChatMessage {
    @Accessor(value="message")
    void setMessage ( String var1 );

    @Accessor(value="message")
    String getMessage ( );
}

