package model.apiversions;

public class ApiKeyModel {
  private short apiKey;
  private short minVersion;
  private short maxVersion;
  private byte tagBuffer;

  public short getApiKey() {
    return apiKey;
  }

  public short getMinVersion() {
    return minVersion;
  }

  public short getMaxVersion() {
    return maxVersion;
  }

  public byte getTagBuffer() {
    return tagBuffer;
  }

  public void setApiKey(short apiKey) {
    this.apiKey = apiKey;
  }

  public void setMinVersion(short minVersion) {
    this.minVersion = minVersion;
  }

  public void setMaxVersion(short maxVersion) {
    this.maxVersion = maxVersion;
  }

  public void setTagBuffer(byte tagBuffer) {
    this.tagBuffer = tagBuffer;
  }

  public int getSize() {
    return 3 * Short.BYTES + Byte.BYTES;
  }
}