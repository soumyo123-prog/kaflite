package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import constant.ErrorCodes;
import constant.ServerConstants;

public class Server {
  private int port;

  public Server(int port) {
    this.port = port;
  }

  public Server() {
    this.port = ServerConstants.DEFAULT_PORT;
  }

  public void start() {
    try (ServerSocket serverSocket = new ServerSocket(this.port)) {
      serverSocket.setReuseAddress(true);

      while (true) {
        try (Socket clientSocket = serverSocket.accept()) {
          DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
          DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

          // Reading the first 4 bytes (message size).
          // &255 is done to ensure that the result is a clean unsigned integer between 0
          // and 255.
          // int requestMessageSize = ((inputStream.read() & 255) << 24) |
          // ((inputStream.read() & 255) << 16) |
          // ((inputStream.read() & 255) << 8) |
          // (inputStream.read() & 255);

          int requestMessageSize = dataInputStream.readInt(); // Modern approach

          byte[] bytes = new byte[requestMessageSize];

          // Outdated approach containing lot of boilerplate code:
          // int totalRead = 0;
          // while (totalRead < requestMessageSize) {
          // int bytesRead = inputStream.read(bytes, totalRead, requestMessageSize -
          // totalRead);
          // if (bytesRead == -1) {
          // break;
          // }
          // totalRead += bytesRead;
          // }

          dataInputStream.readFully(bytes); // Modern approach

          // Outdated approach (prone to error)
          // int correlationId = (((bytes[4] & 255) << 24) |
          // ((bytes[5] & 255) << 16) |
          // ((bytes[6] & 255) << 8) |
          // ((bytes[7] & 255)));

          int correlationId = ByteBuffer.wrap(bytes).getInt(4); // Modern approach

          // DataOutputStream will do the following operations:
          // outputStream.write((value >> 24) & 255);
          // outputStream.write((value >> 16) & 255);
          // outputStream.write((value >> 8) & 255);
          // outputStream.write((value >> 0) & 255);

          int responseMessageSize = 2 * Integer.BYTES + 5 * Short.BYTES;

          dataOutputStream.writeInt(responseMessageSize);
          dataOutputStream.writeInt(correlationId);
          dataOutputStream.writeShort(ErrorCodes.NO_ERROR.getErrorCode());
          dataOutputStream.writeShort(1);
          dataOutputStream.writeShort(18);
          dataOutputStream.writeShort(0);
          dataOutputStream.writeShort(4);
          dataOutputStream.writeInt(0);

          dataOutputStream.flush();
        } catch (IOException e) {
          System.out.println("Client error: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.out.println("Server error: " + e.getMessage());
    }
  }
}
