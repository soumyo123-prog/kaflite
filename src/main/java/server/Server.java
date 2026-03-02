package server;

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
      this.clientSocket = this.serverSocket.accept();

      InputStream inputStream = this.clientSocket.getInputStream();

      byte[] bytes = inputStream.readAllBytes();

      System.out.println("Byte array size: " + bytes.length);

      OutputStream outputStream = this.clientSocket.getOutputStream();
      DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

      dataOutputStream.writeByte(bytes[4]);
      dataOutputStream.writeByte(bytes[5]);
      dataOutputStream.writeByte(bytes[6]);
      dataOutputStream.writeByte(bytes[7]);

      dataOutputStream.flush();
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
