package me.hollow.trollgod.api.mixin.mixins.network;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={CPacketPlayer.class})
public interface AccessorCPacketPlayer {
    @Accessor(value="yaw")
    void setYaw ( float var1 );

    @Accessor(value="pitch")
    void setPitch ( float var1 );

    @Accessor(value="yaw")
    float getYaw ( );

    @Accessor(value="pitch")
    float getPitch ( );
}

