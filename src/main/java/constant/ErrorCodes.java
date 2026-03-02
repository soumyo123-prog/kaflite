package constant;

public enum ErrorCodes {
  UNSUPPORTED_VERSION((short) 35),
  NO_ERROR((short) 0);

  private short errorCode;

  ErrorCodes(short code) {
    this.errorCode = code;
  }

  public short getErrorCode() {
    return this.errorCode;
  }
}
