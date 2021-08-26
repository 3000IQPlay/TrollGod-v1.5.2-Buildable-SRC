package me.hollow.trollgod.client.events;

import tcb.bces.event.Event;

public final class KeyEvent
extends Event {
    private final int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public final int getKey() {
        return this.key;
    }
}

