package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import constant.ServerConstants;

public class Server {
  private ServerSocket serverSocket;
  private Socket clientSocket;

  public Server(int port) {
    start(port);
  }

  public Server() {
    start(ServerConstants.DEFAULT_PORT);
  }

  public void start(int port) {
    try {
      this.serverSocket = new ServerSocket(port);
      this.serverSocket.setReuseAddress(true);

      while (true) {
        this.clientSocket = this.serverSocket.accept();

        InputStream inputStream = this.clientSocket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Reading the first 4 bytes (message size).
        // &255 is done to ensure that the result is a clean unsigned integer between 0
        // and 255.
        // int messageSize = ((inputStream.read() & 255) << 24) |
        // ((inputStream.read() & 255) << 16) |
        // ((inputStream.read() & 255) << 8) |
        // (inputStream.read() & 255);

        int messageSize = dataInputStream.readInt();

        // Not using readAllBytes because it is blocking and waits till end of input.
        // byte[] bytes = inputStream.readAllBytes();

        byte[] bytes = new byte[messageSize];

        // Outdated approach containing lot of boilerplate code:
        // int totalRead = 0;
        // while (totalRead < messageSize) {
        // int bytesRead = inputStream.read(bytes, totalRead, messageSize - totalRead);
        // if (bytesRead == -1) {
        // break;
        // }
        // totalRead += bytesRead;
        // }

        dataInputStream.readFully(bytes);
        int correlationId = (((bytes[4] & 255) << 24) |
            ((bytes[5] & 255) << 16) |
            ((bytes[6] & 255) << 8) |
            ((bytes[7] & 255)));

        OutputStream outputStream = this.clientSocket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeInt(messageSize);
        dataOutputStream.writeInt(correlationId);

        dataOutputStream.flush();
      }
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

  // private void writeIntOutput(int value) throws IOException {
  // OutputStream outputStream = this.clientSocket.getOutputStream();
  // DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

  // dataOutputStream.writeInt(value);
  // dataOutputStream.flush();

  // // DataOutputStream will do the following operations:
  // // outputStream.write((value >> 24) & 255);
  // // outputStream.write((value >> 16) & 255);
  // // outputStream.write((value >> 8) & 255);
  // // outputStream.write((value >> 0) & 255);
  // }
}
