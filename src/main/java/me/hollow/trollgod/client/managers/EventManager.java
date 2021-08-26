package me.hollow.trollgod.client.managers;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.client.events.PacketEvent;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.combat.AutoCrystal;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;

public class EventManager
implements IListener {
    private int ticksPassed;
    private int size = -1;

    public void init() {
        MinecraftForge.EVENT_BUS.register( this );
        TrollGod.INSTANCE.getBus().register(this);
        this.size = TrollGod.INSTANCE.getModuleManager().getModules().size();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() == Minecraft.getMinecraft().player) {
            for (int i = 0; i < this.size; ++i) {
                Module module = TrollGod.INSTANCE.getModuleManager().getModules().get(i);
                if (!module.isEnabled()) continue;
                module.onUpdate();
            }
            TrollGod.INSTANCE.getSafeManager().update();
            TrollGod.INSTANCE.getPopManager().update();
            TrollGod.INSTANCE.getSpeedManager().update();
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onTickHighest(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ++this.ticksPassed;
        }
        if (AutoCrystal.INSTANCE.isEnabled()) {
            AutoCrystal.INSTANCE.onTick();
        }
    }

    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            this.ticksPassed = 0;
        }
    }

    public int ticksPassedSinceSwitch() {
        return this.ticksPassed;
    }

    @Override
    public boolean isListening() {
        return true;
    }
}

