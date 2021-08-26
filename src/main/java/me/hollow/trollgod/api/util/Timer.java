package me.hollow.trollgod.api.util;

public class Timer {
    private long current = -1L;

    public final boolean hasReached(long delay) {
        return System.currentTimeMillis() - this.current >= delay;
    }

    public boolean hasReached(long delay, boolean reset) {
        if (reset) {
            this.reset();
        }
        return System.currentTimeMillis() - this.current >= delay;
    }

    public final void reset() {
        this.current = System.currentTimeMillis();
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.current;
    }
}

