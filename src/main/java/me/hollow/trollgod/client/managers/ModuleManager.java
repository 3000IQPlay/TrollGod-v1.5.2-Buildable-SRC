package me.hollow.trollgod.client.managers;

import me.hollow.trollgod.client.modules.Module;
import me.hollow.trollgod.client.modules.client.*;
import me.hollow.trollgod.client.modules.combat.*;
import me.hollow.trollgod.client.modules.misc.*;
import me.hollow.trollgod.client.modules.movement.*;
import me.hollow.trollgod.client.modules.player.*;
import me.hollow.trollgod.client.modules.visual.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList <> ( );
    private int size = 0;

    public void init() {
        this.register(new HUD());
        this.register(new ClickGui());
        this.register(new PopCounter());
        this.register(new MiddleClick());
        this.register(new Manage());
        this.register(new Colours());
        this.register(new FontModule());
        this.register(new SelfTrap());
        this.register(new KillAura());
        this.register(new AutoArmor());
        this.register(new AutoCrystal());
        this.register(new AutoTrap());
        this.register(new Burrow());
        this.register(new Criticals());
        this.register(new HoleFiller());
        this.register(new Offhand());
        this.register(new AutoFeetPlace());
        this.register(new AntiRegear());
        this.register(new FakePlayer());
        this.register(new PacketCanceller());
        this.register(new MultiTask());
        this.register(new NoRotate());
        this.register(new AutoRespawn());
        this.register(new AntiPush());
        this.register(new ChatSuffix());
        this.register(new ReverseStep());
        this.register(new Speed());
        this.register(new Step());
        this.register(new Strafe());
        this.register(new LiquidTweaks());
        this.register(new SpeedTest());
        this.register(new BehindSpoof());
        this.register(new Interact());
        this.register(new Stacker());
        this.register(new SpeedMine());
        this.register(new AutoTotem());
        this.register(new InstaMine());
        this.register(new AntiVoid());
        this.register(new NoFall());
        this.register(new LavaESP());
        this.register(new BlockHighlight());
        this.register(new EnchantColor());
        this.register(new EntityESP());
        this.register(new HoleESP());
        this.register(new Nametags());
        this.register(new SkyColour());
        this.register(new ViewmodelChanger());
        this.register(new ShulkerPreview());
        this.register(new VoidESP());
        this.register(new NoRender());
        this.register(new Skeleton());
        this.register(new LogOutSpots());
        this.register(new TimeChanger());
        this.register(new Trajectories());
        this.register(new NoArmorRender());
        this.register(new Tracers());
        this.modules.forEach(Module::onLoad);
        this.size = this.modules.size();
    }

    public int getSize() {
        return this.size;
    }

    private void register(Module module) {
        this.modules.add(module);
    }

    public final List<Module> getModules() {
        return this.modules;
    }

    public final Module getModuleByClass(Class<?> clazz) {
        Module module = null;
        int size = this.modules.size();
        for (Module m : this.modules) {
            if ( m.getClass ( ) != clazz ) continue;
            module = m;
        }
        return module;
    }

    public final List<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> list = new ArrayList <> ( );
        for (Module module : this.modules) {
            if (!module.getCategory().equals( category )) continue;
            list.add(module);
        }
        return list;
    }

    public final Module getModuleByLabel(String label) {
        Module module = null;
        for (Module m : this.modules) {
            if (!m.getLabel().equalsIgnoreCase(label)) continue;
            module = m;
        }
        return module;
    }
}

