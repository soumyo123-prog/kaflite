package utils;

import java.io.ByteArrayOutputStream;

import constant.ServerConstants;

public class EncodingDecodingUtil {
  public static byte[] encodeToVarint(int value) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    while ((value & ServerConstants.REMOVE_FIRST_SEVEN_BITS) != 0) {
      bos.write((value & ServerConstants.TAKE_FIRST_SEVEN_BITS) | ServerConstants.MARK_CONTINUATION_BIT);
      value = value >> 7;
    }

    bos.write(value & ServerConstants.TAKE_FIRST_SEVEN_BITS);

    return bos.toByteArray();
  }
}
