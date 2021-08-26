package me.hollow.trollgod.api.util.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
    public static void submitToExecutor(Runnable runnable) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(runnable);
        executor.shutdown();
    }
}

