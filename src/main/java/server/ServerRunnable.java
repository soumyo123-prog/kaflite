package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import model.RequestModel;
import request.RequestHandlerManager;

public class ServerRunnable implements Runnable {
  private Socket clientSocket;
  private RequestHandlerManager requestHandlerManager;

  public ServerRunnable(Socket clientSocket, RequestHandlerManager requestHandlerManager) {
    this.clientSocket = clientSocket;
    this.requestHandlerManager = requestHandlerManager;
  }

  @Override
  public void run() {
    try {
      System.out.println("DEBUG: Started processing request from " + clientSocket.getPort());
      DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

      // Reading the first 4 bytes (message size).
      // &255 is done to ensure that the result is a clean unsigned integer between 0
      // and 255.
      // int requestMessageSize = ((inputStream.read() & 255) << 24) |
      // ((inputStream.read() & 255) << 16) |
      // ((inputStream.read() & 255) << 8) |
      // (inputStream.read() & 255);

      RequestModel request = new RequestModel();

      request.setMessageSize(dataInputStream.readInt()); // Modern approach

      byte[] bytes = new byte[request.getMessageSize()];

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

      request.setApiKey((int) ByteBuffer.wrap(bytes).getShort(0));
      request.setApiVersion((int) ByteBuffer.wrap(bytes).getShort(2));
      request.setCorrelationId(ByteBuffer.wrap(bytes).getInt(4));

      requestHandlerManager.manage(request, dataOutputStream);

      // DataOutputStream will do the following operations:
      // outputStream.write((value >> 24) & 255);
      // outputStream.write((value >> 16) & 255);
      // outputStream.write((value >> 8) & 255);
      // outputStream.write((value >> 0) & 255);
      System.out.println("DEBUG: Finished processing request from " + clientSocket.getPort());
    } catch (IllegalArgumentException e) {
      System.out.println("Error in processing the request: " + e.getMessage());
    } catch (EOFException e) {
      System.out.println("Client closed the connection abruptly.");
    } catch (IOException e) {
      System.out.println("Client error: " + e.getMessage());
    } finally {
      try {
        this.clientSocket.close();
      } catch (IOException e) {
        System.out.println("Client error: " + e.getMessage());
      }
    }
  }
}
