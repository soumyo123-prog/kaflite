package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import constant.ServerConstants;
import request.RequestHandlerManager;

public class ServerMain {
  private RequestHandlerManager requestHandlerManager;
  private ServerSocket serverSocket;
  private ServerThreadPool threadPool;
  private int port;
  private volatile boolean running;

  public ServerMain(RequestHandlerManager requestHandlerManager, int port) {
    this.requestHandlerManager = requestHandlerManager;
    this.port = port;
  }

  public ServerMain(RequestHandlerManager requestHandlerManager) {
    this.requestHandlerManager = requestHandlerManager;
    this.port = ServerConstants.DEFAULT_PORT;
  }

  public void start() {
    this.running = true;

    try (ServerSocket socket = new ServerSocket(this.port)) {
      this.serverSocket = socket;
      this.serverSocket.setReuseAddress(true);

      ServerThreadPool serverThreadPool = new ServerThreadPool(50);
      this.threadPool = serverThreadPool;

      while (this.running) {
        Socket clientSocket = this.serverSocket.accept();
        serverThreadPool.submit(new ServerRunnable(clientSocket, requestHandlerManager));
      }
    } catch (IOException e) {
      System.out.println("Server error: " + e.getMessage());
    } finally {
      this.running = false;
      threadPool.shutdown();
    }
  }

  // public void stop() {
  // this.running = false;
  // try {
  // if (serverSocket != null) {
  // serverSocket.close();
  // }
  // } catch (IOException e) {
  // System.err.println("Error while closing server: " + e.getMessage());
  // }
  // }
}
