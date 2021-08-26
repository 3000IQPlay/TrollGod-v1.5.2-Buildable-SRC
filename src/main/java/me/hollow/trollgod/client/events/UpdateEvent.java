package me.hollow.trollgod.client.events;

import tcb.bces.event.Event;

public final class UpdateEvent
extends Event {
    private final int stage;

    public UpdateEvent(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return this.stage;
    }
}

