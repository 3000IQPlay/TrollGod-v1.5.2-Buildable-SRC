package me.hollow.trollgod.client.managers;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.interfaces.Minecraftable;
import me.hollow.trollgod.api.util.MessageUtil;
import me.hollow.trollgod.client.events.PacketEvent;
import me.hollow.trollgod.client.modules.client.PopCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class PopManager
implements Minecraftable,
IListener {
    private final Map<String, Integer> popMap = new HashMap <> ( );

    public void update() {
        int size = PopManager.mc.world.playerEntities.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer player = PopManager.mc.world.playerEntities.get(i);
            if (!(player.getHealth() <= 0.0f) || !this.popMap.containsKey(player.getName())) continue;
            if (PopCounter.getInstance().isEnabled()) {
                if ( PopCounter.notify.getValue ( ) ) {
                    TrollGod.INSTANCE.getNotificationManager().addNotification(player.getName() + " died after popping their " + this.popMap.get(player.getName()) + this.getNumberStringThing(this.popMap.get(player.getName())) + " totem.");
                }
                MessageUtil.sendClientMessage(player.getName() + " died after popping their " + this.popMap.get(player.getName()) + this.getNumberStringThing(this.popMap.get(player.getName())) + " totem.", player.getEntityId());
            }
            this.popMap.remove(player.getName(), this.popMap.get(player.getName()));
        }
    }

    public String getNumberStringThing(int number) {
        if (number > 3) {
            return "th";
        }
        switch (number) {
            case 2: {
                return "nd";
            }
            case 3: {
                return "rd";
            }
        }
        return "";
    }

    @Subscribe
    public void onPacket(PacketEvent.Receive event) {
        SPacketEntityStatus packet;
        if (PopManager.mc.player == null || PopManager.mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 35) {
            Entity entity = packet.getEntity( PopManager.mc.world );
            if (this.popMap.get(entity.getName()) == null) {
                this.popMap.put(entity.getName(), 1);
                if (PopCounter.getInstance().isEnabled()) {
                    if ( PopCounter.notify.getValue ( ) ) {
                        TrollGod.INSTANCE.getNotificationManager().addNotification(entity.getName() + " popped a totem.");
                    }
                    MessageUtil.sendClientMessage(entity.getName() + " popped a totem.", entity.getEntityId());
                }
            } else if (this.popMap.get(entity.getName()) != null) {
                int popCounter = this.popMap.get(entity.getName());
                int newPopCounter = popCounter + 1;
                this.popMap.put(entity.getName(), newPopCounter);
                if (PopCounter.getInstance().isEnabled()) {
                    if ( PopCounter.notify.getValue ( ) ) {
                        TrollGod.INSTANCE.getNotificationManager().addNotification(entity.getName() + " popped their " + newPopCounter + this.getNumberStringThing(newPopCounter) + " totem.");
                    }
                    MessageUtil.sendClientMessage(entity.getName() + " popped their " + newPopCounter + this.getNumberStringThing(newPopCounter) + " totem.", entity.getEntityId());
                }
            }
        }
    }

    public final Map<String, Integer> getPopMap() {
        return this.popMap;
    }

    public void init() {
        TrollGod.INSTANCE.getBus().register(this);
    }

    @Override
    public boolean isListening() {
        return true;
    }
}

