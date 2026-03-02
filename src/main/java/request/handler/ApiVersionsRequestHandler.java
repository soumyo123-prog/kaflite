package request.handler;

import java.io.DataOutputStream;
import java.io.IOException;

import constant.ErrorCodes;
import model.RequestModel;
import request.RequestHandler;

public class ApiVersionsRequestHandler implements RequestHandler {
  public static final Integer MIN_SUPPORTED_VERSION = 0;
  public static final Integer MAX_SUPPORTED_VERSION = 4;

  @Override
  public Integer getApiKey() {
    return 18;
  }

  @Override
  public void handle(RequestModel request, DataOutputStream dataOutputStream) throws IOException {
    if (!(request.getApiVersion() >= MIN_SUPPORTED_VERSION && request.getApiVersion() <= MAX_SUPPORTED_VERSION)) {
      dataOutputStream.writeInt(Integer.BYTES + Short.BYTES);
      dataOutputStream.writeInt(request.getCorrelationId());
      dataOutputStream.writeShort(ErrorCodes.UNSUPPORTED_VERSION.getErrorCode());
      dataOutputStream.flush();
      return;
    }

    dataOutputStream.writeInt(2 * Integer.BYTES + 4 * Short.BYTES + 3 * Byte.BYTES);
    dataOutputStream.writeInt(request.getCorrelationId());
    dataOutputStream.writeShort(ErrorCodes.NO_ERROR.getErrorCode());
    dataOutputStream.writeByte(1);
    dataOutputStream.writeShort(getApiKey());
    dataOutputStream.writeShort(MIN_SUPPORTED_VERSION);
    dataOutputStream.writeShort(MAX_SUPPORTED_VERSION);
    dataOutputStream.writeByte(0);
    dataOutputStream.writeInt(0);
    dataOutputStream.writeByte(0);

    dataOutputStream.flush();
  }

}
