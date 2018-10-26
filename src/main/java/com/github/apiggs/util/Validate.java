package com.github.apiggs.util;

import com.google.common.base.Strings;

public class Validate {

    public static void notBlank(String text, String message) {
        if (Strings.isNullOrEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void between(int num, int min, int max, String message) {
        if (num < min || num > max) {
            throw new IllegalArgumentException(message);
        }

    }

}
