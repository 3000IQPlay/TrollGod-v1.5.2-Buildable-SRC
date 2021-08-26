package me.hollow.trollgod.client.modules.combat;

import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.client.events.PacketEvent;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import tcb.bces.listener.Subscribe;

import java.util.Objects;

@ModuleManifest(label="Criticals", category=Module.Category.COMBAT, color=-12582800)
public class Criticals
extends Module {
    private final Setting<Boolean> smallPacket = this.register( new Setting <> ( "small packet" , false ));
    private final Setting<Integer> packets = this.register( new Setting <> ( "Packets" , 2 , 1 , 5 , v -> ! this.smallPacket.getValue ( ) ));
    private final double[] packetArray = new double[]{0.11, 0.11, 0.1100013579, 0.1100013579, 0.1100013579, 0.1100013579};

    @Subscribe
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            if (!this.mc.player.onGround) {
                return;
            }
            CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld( this.mc.world ) instanceof EntityLivingBase) {
                if ( this.smallPacket.getValue ( ) ) {
                    Objects.requireNonNull ( this.mc.getConnection ( ) ).sendPacket( new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.01, this.mc.player.posZ, false) );
                } else {
                    for (int i = 0; i < this.packets.getValue(); ++i) {
                        Objects.requireNonNull ( this.mc.getConnection ( ) ).sendPacket( new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + this.packetArray[i], this.mc.player.posZ, false) );
                    }
                }
                Objects.requireNonNull ( this.mc.getConnection ( ) ).sendPacket( new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, false) );
            }
        }
    }
}

