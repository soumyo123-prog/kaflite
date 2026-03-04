package request.handler;

import java.io.DataOutputStream;
import java.io.IOException;

import constant.ErrorCodes;
import constant.ServerConstants;
import model.RequestModel;
import model.apiversions.ApiKeyModel;
import model.apiversions.ApiVersionsResponseModel;
import request.RequestHandler;
import utils.EncodingDecodingUtil;

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

    ApiVersionsResponseModel apiVersionsResponse = new ApiVersionsResponseModel();

    apiVersionsResponse.setCorrelationId(request.getCorrelationId());
    apiVersionsResponse.setErrorCode(ErrorCodes.NO_ERROR.getErrorCode());
    apiVersionsResponse.addApiKey(getApiKey(), ServerConstants.MIN_SUPPORTED_VERSION,
        ServerConstants.MAX_SUPPORTED_VERSION, (byte) 0);
    apiVersionsResponse.addApiKey(getApiKey(), ServerConstants.MIN_SUPPORTED_VERSION,
        ServerConstants.MAX_SUPPORTED_VERSION, (byte) 0);
    apiVersionsResponse.setEncodedLength(EncodingDecodingUtil.encodeToVarint(apiVersionsResponse.getLength() + 1));
    apiVersionsResponse.setThrottleTime(0);
    apiVersionsResponse.setTagBuffer((byte) 0);

    int messageSize = apiVersionsResponse.getSize();

    dataOutputStream.writeInt(messageSize);
    writeResponseToDataOutputStream(apiVersionsResponse, dataOutputStream);
    dataOutputStream.flush();
  }

  private void writeResponseToDataOutputStream(ApiVersionsResponseModel response, DataOutputStream dataOutputStream)
      throws IOException {
    dataOutputStream.writeInt(response.getCorrelationId());
    dataOutputStream.writeShort(response.getErrorCode());
    for (byte i : response.getVarintEncodedLength()) {
      dataOutputStream.writeByte(i);
    }
    for (ApiKeyModel apiKeyModel : response.getApiKeys()) {
      dataOutputStream.writeShort(apiKeyModel.getApiKey());
      dataOutputStream.writeShort(apiKeyModel.getMinVersion());
      dataOutputStream.writeShort(apiKeyModel.getMaxVersion());
      dataOutputStream.writeByte(apiKeyModel.getTagBuffer());
    }
    dataOutputStream.writeInt(response.getThrottleTime());
    dataOutputStream.writeByte(response.getTagBuffer());
  }
}
