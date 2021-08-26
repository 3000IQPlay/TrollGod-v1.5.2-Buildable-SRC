package me.hollow.trollgod.api.mixin.mixins.network;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketPlayerPosLook.class})
public interface AccessorSPacketPlayerPosLook {
    @Accessor(value="yaw")
    void setYaw ( float var1 );

    @Accessor(value="pitch")
    void setPitch ( float var1 );
}

