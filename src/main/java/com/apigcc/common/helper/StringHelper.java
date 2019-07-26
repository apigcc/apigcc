package com.apigcc.common.helper;

import com.google.common.base.Strings;

public class StringHelper {

    public static boolean isBlank(String text) {
        return Strings.isNullOrEmpty(text) || Strings.isNullOrEmpty(text.trim());
    }

    public static boolean nonBlank(String text) {
        return !isBlank(text);
    }

    public static boolean isBlank(Object text) {
        if(text instanceof String){
            return isBlank(((String) text));
        }
        return isBlank(String.valueOf(text));
    }

    public static boolean nonBlank(Object text) {
        if(text instanceof String){
            return nonBlank(((String) text));
        }
        return nonBlank(String.valueOf(text));
    }

}
