package constant;

public class ServerConstants {
  public static final Integer DEFAULT_PORT = 9092;
  public static final Integer MESSAGE_SIZE = 0;
  public static final Short MIN_SUPPORTED_VERSION = 0;
  public static final Short MAX_SUPPORTED_VERSION = 4;
  public static final Integer REMOVE_FIRST_SEVEN_BITS = 0xFFFFFF80;
  public static final Integer TAKE_FIRST_SEVEN_BITS = 0x7F;
  public static final Integer MARK_CONTINUATION_BIT = 0x80;
  public static final Runnable POISON_PILL = () -> {
  };
}
