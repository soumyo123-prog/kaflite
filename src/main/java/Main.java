import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 9092;
    try {
      serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept();

      InputStream inputStream = clientSocket.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader inSocket = new BufferedReader(inputStreamReader);

      OutputStream outputStream = clientSocket.getOutputStream();
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

      System.out.println("Client says: " + inSocket.readLine());

      int messageSize = 0;
      int correlationId = 7;

      outputStream.write(messageSize);
      outputStream.write(correlationId);
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } finally {
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }
}
