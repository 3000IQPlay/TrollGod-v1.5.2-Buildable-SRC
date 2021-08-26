package me.hollow.trollgod.client.managers;

import com.google.common.base.Strings;
import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.interfaces.Minecraftable;
import me.hollow.trollgod.client.events.ConnectionEvent;
import me.hollow.trollgod.client.events.PacketEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

public class TPSManager
implements Minecraftable,
IListener {
    public static float TPS = 20.0f;
    public static long lastUpdate = -1L;
    public static float[] tpsCounts = new float[10];
    public static DecimalFormat format = new DecimalFormat("##.0#");

    public static void update() {
        float tps;
        long currentTime = System.currentTimeMillis();
        if (lastUpdate == -1L) {
            lastUpdate = currentTime;
            return;
        }
        long timeDiff = currentTime - lastUpdate;
        float tickTime = timeDiff / 20L;
        if (tickTime == 0.0f) {
            tickTime = 50.0f;
        }
        if ((tps = 1000.0f / tickTime) > 20.0f) {
            tps = 20.0f;
        }
        System.arraycopy(tpsCounts, 0, tpsCounts, 1, tpsCounts.length - 1);
        TPSManager.tpsCounts[0] = tps;
        double total = 0.0;
        for (float f : tpsCounts) {
            total += f;
        }
        if ((total /= tpsCounts.length ) > 20.0) {
            total = 20.0;
        }
        TPS = Float.parseFloat(format.format(total));
        lastUpdate = currentTime;
    }

    public void reset() {
        Arrays.fill(tpsCounts, 20.0f);
        TPS = 20.0f;
    }

    public void init() {
        TrollGod.INSTANCE.getBus().register(this);
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            TPSManager.update();
        }
        if (event.getPacket() instanceof SPacketPlayerListItem) {
            if (TPSManager.mc.world == null || TPSManager.mc.player == null) {
                return;
            }
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals( packet.getAction() ) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals( packet.getAction() )) {
                return;
            }
            for (SPacketPlayerListItem.AddPlayerData data : packet.getEntries()) {
                if (data == null || Strings.isNullOrEmpty( data.getProfile().getName() ) && data.getProfile().getId() == null) continue;
                UUID id = data.getProfile().getId();
                switch (packet.getAction()) {
                    case ADD_PLAYER: {
                        String name = data.getProfile().getName();
                        TrollGod.INSTANCE.getBus().post(new ConnectionEvent(0, id, name));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = TPSManager.mc.world.getPlayerEntityByUUID(id);
                        if (entity != null) {
                            String logoutName = entity.getName();
                            TrollGod.INSTANCE.getBus().post(new ConnectionEvent(1, entity, id, logoutName));
                            break;
                        }
                        TrollGod.INSTANCE.getBus().post(new ConnectionEvent(2, id, null));
                    }
                }
            }
        }
    }

    public final float getTpsFactor() {
        return 20.0f / TPS;
    }

    public final float getTPS() {
        return TPS;
    }

    public int getPing() {
        if (TPSManager.mc.player == null || TPSManager.mc.world == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.getConnection().getGameProfile().getId()) == null) {
            return -1;
        }
        return mc.getConnection().getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
    }

    @Override
    public boolean isListening() {
        return true;
    }
}

