package server;

import java.util.concurrent.LinkedBlockingQueue;
import constant.ServerConstants;

public class ServerWorkerThread extends Thread {
  private LinkedBlockingQueue<Runnable> taskQueue;
  private volatile boolean isRunning = true;

  public ServerWorkerThread(LinkedBlockingQueue<Runnable> taskQueue) {
    this.taskQueue = taskQueue;
  }

  @Override
  public void run() {
    try {
      while (isRunning) {
        Runnable task = taskQueue.take();
        if (task == ServerConstants.POISON_PILL) {
          break;
        }
        task.run();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void shutdown() {
    isRunning = false;
    this.interrupt();
  }
}
