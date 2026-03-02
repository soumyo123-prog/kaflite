import request.RequestHandlerManager;
import server.Server;

public class Kaflite {
  public static void main(String[] args) {
    new Server(new RequestHandlerManager(), 9092).start();
  }
}
