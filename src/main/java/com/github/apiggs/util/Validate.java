package com.github.apiggs.util;

import com.google.common.base.Strings;

public class Validate {

    public static boolean isBlank(String text) {
        return Strings.isNullOrEmpty(text) || Strings.isNullOrEmpty(text.trim());
    }

    public static void notBlank(String text, String message) {
        if (Strings.isNullOrEmpty(text) || Strings.isNullOrEmpty(text.trim())) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void between(int num, int min, int max, String message) {
        if (num < min || num > max) {
            throw new IllegalArgumentException(message);
        }

    }

}
