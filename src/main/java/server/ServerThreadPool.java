package server;

import java.util.concurrent.LinkedBlockingQueue;

import constant.ServerConstants;

public class ServerThreadPool {
  private LinkedBlockingQueue<Runnable> taskQueue;
  private ServerWorkerThread[] workers;

  public ServerThreadPool(int size) {
    taskQueue = new LinkedBlockingQueue<>();
    workers = new ServerWorkerThread[size];

    for (int i = 0; i < size; i++) {
      workers[i] = new ServerWorkerThread(taskQueue);
      workers[i].start();
    }
  }

  public void submit(Runnable task) {
    taskQueue.offer(task);
  }

  public void shutdown() {
    for (int i = 0; i < workers.length; i++) {
      taskQueue.offer(ServerConstants.POISON_PILL);
    }
  }
}
