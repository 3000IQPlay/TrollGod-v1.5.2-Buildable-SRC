package me.hollow.trollgod.client.modules.movement;

import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.ModuleManifest;

@ModuleManifest(label="LiquidTweaks", listen=false, category=Module.Category.MOVEMENT)
public class LiquidTweaks
extends Module {
    private final Setting<Boolean> vertical = this.register( new Setting <> ( "Vertical" , true ));
    private final Setting<Boolean> weird = this.register( new Setting <> ( "Weird" , false ));

    @Override
    public void onUpdate() {
        if (this.mc.player.isInLava() && ! this.weird.getValue ( ) && this.vertical.getValue ( ) && !this.mc.player.collidedVertically && this.mc.gameSettings.keyBindSneak.isKeyDown()) {
            this.mc.player.motionY -= 0.06553;
        }
    }
}

