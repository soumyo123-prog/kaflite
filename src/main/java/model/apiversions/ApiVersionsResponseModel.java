package model.apiversions;

import java.util.ArrayList;
import java.util.List;

public class ApiVersionsResponseModel {
  private int correlationId;
  private short errorCode;
  private byte[] varintEncodedLength;
  private List<ApiKeyModel> apiKeys;
  private int throttleTime;
  private byte tagBuffer;

  public ApiVersionsResponseModel() {
    apiKeys = new ArrayList<>();
  }

  public int getSize() {
    int apiKeysSize = apiKeys == null ? 0
        : apiKeys.stream()
            .mapToInt(ApiKeyModel::getSize)
            .sum();

    return 2 * Integer.BYTES + Short.BYTES + Byte.BYTES + Byte.BYTES * varintEncodedLength.length + apiKeysSize;
  }

  public void addApiKey(short apiKey, short minVersion, short maxVersion, byte tagBuffer) {
    ApiKeyModel model = new ApiKeyModel();

    model.setApiKey(apiKey);
    model.setMinVersion(minVersion);
    model.setMaxVersion(maxVersion);
    model.setTagBuffer(tagBuffer);

    apiKeys.add(model);
  }

  public int getCorrelationId() {
    return correlationId;
  }

  public short getErrorCode() {
    return errorCode;
  }

  public byte[] getVarintEncodedLength() {
    return varintEncodedLength;
  }

  public int getLength() {
    return apiKeys.size();
  }

  public List<ApiKeyModel> getApiKeys() {
    return apiKeys;
  }

  public int getThrottleTime() {
    return throttleTime;
  }

  public byte getTagBuffer() {
    return tagBuffer;
  }

  public void setCorrelationId(int correlationId) {
    this.correlationId = correlationId;
  }

  public void setErrorCode(short errorCode) {
    this.errorCode = errorCode;
  }

  public void setEncodedLength(byte[] varintEncodedLength) {
    this.varintEncodedLength = varintEncodedLength;
  }

  public void setThrottleTime(int throttleTime) {
    this.throttleTime = throttleTime;
  }

  public void setTagBuffer(byte tagBuffer) {
    this.tagBuffer = tagBuffer;
  }
}
