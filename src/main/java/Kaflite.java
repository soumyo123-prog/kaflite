import server.Server;

public class Kaflite {
  public static void main(String[] args) {
    new Server(9092).start();
  }
}
