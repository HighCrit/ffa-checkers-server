package com.highcrit.ffacheckers.socket.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  public void scheduleTask(Runnable task, int seconds) {
    this.scheduler.schedule(task, seconds, TimeUnit.SECONDS);
  }
}
