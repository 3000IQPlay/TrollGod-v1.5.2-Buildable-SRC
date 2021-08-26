package me.hollow.trollgod.client.events;

import me.hollow.trollgod.api.property.Setting;
import me.hollow.trollgod.client.modules.Module;
import tcb.bces.event.Event;

public class ClientEvent
extends Event {
    private Module module;
    private Setting setting;
    private int stage;

    public ClientEvent(Setting setting) {
        this.setting = setting;
    }

    public ClientEvent(int stage) {
        this.stage = stage;
    }

    public Module getModule() {
        return this.module;
    }

    public Setting getSetting() {
        return this.setting;
    }

    public int getStage() {
        return this.stage;
    }
}

