package com.highcrit.ffacheckers.socket.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  /**
   * Schedule a task to be executed after a specified amount of seconds
   * @param task task to executed
   * @param seconds seconds till execution
   */
  public void scheduleTask(Runnable task, int seconds) {
    this.scheduler.schedule(task, seconds, TimeUnit.SECONDS);
  }
}
