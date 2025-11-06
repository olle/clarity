package clarity.infrastructure.utils;

import java.nio.charset.StandardCharsets;

public final class Utils {

  private Utils() {
    // HIDDEN
  }

  public static byte[] toBytes(String string) {
    return string.getBytes(StandardCharsets.UTF_8);
  }
}
