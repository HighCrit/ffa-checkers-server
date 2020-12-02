package com.highcrit.ffacheckers.socket.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public ScheduledFuture<?> scheduleTask(Runnable task, int seconds) {
        return this.scheduler.schedule(task, seconds, TimeUnit.SECONDS);
    }

    public ScheduledFuture<?> scheduleTaskAtInterval(Runnable task, int seconds) {
        return this.scheduler.scheduleAtFixedRate(task, 1, seconds, TimeUnit.SECONDS);
    }
}
