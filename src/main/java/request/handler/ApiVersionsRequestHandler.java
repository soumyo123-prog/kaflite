package request.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import constant.ErrorCodes;
import constant.ServerConstants;
import model.RequestModel;
import request.RequestHandler;

public class ApiVersionsRequestHandler implements RequestHandler {
  @Override
  public Short getApiKey() {
    return 18;
  }

  @Override
  public void handle(RequestModel request, DataOutputStream dataOutputStream) throws IOException {
    if (!(request.getApiVersion() >= ServerConstants.MIN_SUPPORTED_VERSION
        && request.getApiVersion() <= ServerConstants.MAX_SUPPORTED_VERSION)) {
      dataOutputStream.writeInt(Integer.BYTES + Short.BYTES);
      dataOutputStream.writeInt(request.getCorrelationId());
      dataOutputStream.writeShort(ErrorCodes.UNSUPPORTED_VERSION.getErrorCode());
      dataOutputStream.flush();
      return;
    }

    int messageSize = 0;

    int correlationId = request.getCorrelationId();
    messageSize += Integer.BYTES;

    short errorCode = ErrorCodes.NO_ERROR.getErrorCode();
    messageSize += Short.BYTES;

    int length = 2;
    byte[] lengthVarint = encodeToVarint(length);
    messageSize += Byte.BYTES * lengthVarint.length;

    short apiKey = getApiKey();
    messageSize += Short.BYTES;

    short minSupportedVersion = ServerConstants.MIN_SUPPORTED_VERSION;
    messageSize += Short.BYTES;

    short maxSupportedVersion = ServerConstants.MAX_SUPPORTED_VERSION;
    messageSize += Short.BYTES;

    byte arrTagBuffer = 0;
    messageSize += Byte.BYTES;

    int throttleTime = 0;
    messageSize += Byte.BYTES;

    byte tagBuffer = 0;
    messageSize += Byte.BYTES;

    dataOutputStream.writeInt(messageSize);
    dataOutputStream.writeInt(correlationId);
    dataOutputStream.writeShort(errorCode);

    for (byte i : lengthVarint) {
      dataOutputStream.writeByte(i);
    }

    dataOutputStream.writeShort(apiKey);
    dataOutputStream.writeShort(minSupportedVersion);
    dataOutputStream.writeShort(maxSupportedVersion);
    dataOutputStream.writeByte(arrTagBuffer);
    dataOutputStream.writeInt(throttleTime);
    dataOutputStream.writeByte(tagBuffer);

    dataOutputStream.flush();
  }

  private byte[] encodeToVarint(int value) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    int curr;

    while ((value & ServerConstants.REMOVE_FIRST_SEVEN_BITS) != 0) {
      curr = (value & ServerConstants.TAKE_FIRST_SEVEN_BITS) | ServerConstants.MARK_CONTINUATION_BIT;
      bos.write(curr);
      value = value >> 7;
    }

    curr = value & ServerConstants.TAKE_FIRST_SEVEN_BITS;
    bos.write(curr);

    return bos.toByteArray();
  }

}
