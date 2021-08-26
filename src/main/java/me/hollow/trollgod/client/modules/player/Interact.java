package me.hollow.trollgod.client.modules.player;

import me.hollow.trollgod.TrollGod;
import me.hollow.trollgod.api.mixin.accessors.IMinecraft;
import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.client.events.UpdateEvent;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Interact", category=Module.Category.PLAYER, color=10858157)
public class Interact
extends Module {
    private final Setting<Boolean> xp = this.register( new Setting <> ( "XP" , true ));
    private final Setting<Boolean> footXp = this.register( new Setting <> ( "Foot XP" , true , v -> this.xp.getValue ( ) ));
    private final Setting<Boolean> blocks = this.register( new Setting <> ( "Blocks" , true ));

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.getStage() == 0 && (this.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE && this.xp.getValue ( ) || this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock && this.blocks.getValue ( ) )) {
            if (this.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE && this.footXp.getValue ( ) ) {
                TrollGod.INSTANCE.getRotationManager().setPitch(90.0f);
            }
            ((IMinecraft)this.mc).setRightClickDelayTimer(0);
        }
    }
}

