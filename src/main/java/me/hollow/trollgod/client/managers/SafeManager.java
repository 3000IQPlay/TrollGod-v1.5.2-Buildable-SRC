package me.hollow.trollgod.client.managers;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import me.hollow.trollgod.api.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;

public class SafeManager
implements Minecraftable {
    private boolean safe;

    public void update() {
        this.safe = true;
        float maxDamage = 0.5f;
        int size = SafeManager.mc.world.loadedEntityList.size();
        for (int i = 0; i < size; ++i) {
            float damage;
            Entity entity = SafeManager.mc.world.loadedEntityList.get(i);
            if (!(entity instanceof EntityEnderCrystal) || SafeManager.mc.player.getDistanceSq(entity) > 100.0 || (damage = EntityUtil.calculate(entity.posX, entity.posY, entity.posZ, SafeManager.mc.player )) < maxDamage) continue;
            maxDamage = damage;
            if (damage + 2.0f < EntityUtil.getHealth( SafeManager.mc.player )) continue;
            this.safe = false;
        }
    }

    public boolean isSafe() {
        return this.safe;
    }
}

