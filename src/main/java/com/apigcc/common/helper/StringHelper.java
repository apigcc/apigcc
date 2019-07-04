package com.apigcc.common.helper;

import com.google.common.base.Strings;

public class StringHelper {

    public static boolean isBlank(String text) {
        return Strings.isNullOrEmpty(text) || Strings.isNullOrEmpty(text.trim());
    }

    public static boolean nonBlank(String text) {
        return !isBlank(text);
    }

}
