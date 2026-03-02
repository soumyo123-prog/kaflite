import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  private static OutputStream outputStream;

  private static void writeFourBytes(int value) throws IOException {
    outputStream.write((value >> 24) & 255);
    outputStream.write((value >> 16) & 255);
    outputStream.write((value >> 8) & 255);
    outputStream.write((value >> 0) & 255);
  }

  public static void main(String[] args) {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 9092;
    try {
      serverSocket = new ServerSocket(port);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept();

      InputStream is = clientSocket.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(is);
      BufferedReader inSocket = new BufferedReader(inputStreamReader);

      OutputStream os = clientSocket.getOutputStream();
      outputStream = os;

      System.out.println("Client says: " + inSocket.readLine());

      int messageSize = 0;
      int correlationId = 7;

      writeFourBytes(messageSize);
      outputStream.flush();

      writeFourBytes(correlationId);
      outputStream.flush();
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
