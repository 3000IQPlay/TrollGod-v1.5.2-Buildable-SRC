package me.hollow.trollgod.client.events;

import net.minecraft.network.Packet;
import tcb.bces.event.EventCancellable;

public class PacketEvent
extends EventCancellable {
    private final Packet < ? extends net.minecraft.network.INetHandler > packet;

    public PacketEvent( Packet < ? extends net.minecraft.network.INetHandler > packet) {
        this.packet = packet;
    }

    public final
    Packet < ? extends net.minecraft.network.INetHandler > getPacket() {
        return this.packet;
    }

    public static final class Send
    extends PacketEvent {
        public Send( Packet < ? extends net.minecraft.network.INetHandler > packet) {
            super(packet);
        }
    }

    public static final class Receive
    extends PacketEvent {
        public Receive( Packet < ? extends net.minecraft.network.INetHandler > packet) {
            super(packet);
        }
    }
}

