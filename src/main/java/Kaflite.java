import request.RequestHandlerManager;
import server.ServerMain;

public class Kaflite {
  public static void main(String[] args) {
    ServerMain server = new ServerMain(new RequestHandlerManager(), 9092);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Shutdown signal received...");
      server.stop();
    }));

    server.start();
  }
}
