package com.highcrit.ffacheckers.socket.utils;

import java.util.Random;

public class StringUtil {
    private static final Random random = new Random();
    private static final int leftLimit = 65; // char 'A'
    private static final int rightLimit = 90; // char 'Z'

    /**
     * Regenerates a random string of given length with charset defined by leftLimit
     * and rightLimit.
     *
     * @param length
     *         length of random string
     *
     * @return random string of given length
     */
    public static String generateCode(final int length) {
        return random.ints(leftLimit, rightLimit + 1).limit(length).collect(StringBuilder::new,
                StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}
