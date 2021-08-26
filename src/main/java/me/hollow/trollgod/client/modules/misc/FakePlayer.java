package me.hollow.trollgod.client.modules.misc;

import com.mojang.authlib.GameProfile;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

@ModuleManifest(label="FakePlayer", listen=false, category=Module.Category.MISC, color=-16711936)
public class FakePlayer
extends Module {
    private EntityOtherPlayerMP entityOtherPlayerMP = null;

    @Override
    public void onEnable() {
        if (this.mc.player == null) {
            return;
        }
        this.entityOtherPlayerMP = new EntityOtherPlayerMP( this.mc.world , new GameProfile(UUID.fromString("cc72ff00-a113-48f4-be18-2dda8db52355"), "is pig"));
        this.entityOtherPlayerMP.copyLocationAndAnglesFrom( this.mc.player );
        this.entityOtherPlayerMP.inventory = this.mc.player.inventory;
        this.mc.world.spawnEntity( this.entityOtherPlayerMP );
    }

    @Override
    public void onDisable() {
        if (this.entityOtherPlayerMP != null) {
            this.mc.world.removeEntity( this.entityOtherPlayerMP );
        }
    }
}

