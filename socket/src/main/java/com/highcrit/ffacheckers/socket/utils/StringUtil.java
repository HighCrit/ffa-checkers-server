package com.highcrit.ffacheckers.socket.utils;

import java.util.Random;

public class StringUtil {
  private static final Random random = new Random();
  private static final int LEFT_LIMIT = 65; // char 'A'
  private static final int RIGHT_LIMIT = 90; // char 'Z'

  private StringUtil() {
    throw new IllegalStateException("Utility Class");
  }

  /**
   * Regenerates a random string of given length with charset defined by leftLimit and rightLimit.
   *
   * @param length length of random string
   * @return random string of given length
   */
  public static String generateCode(final int length) {
    return random
        .ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}
