package com.apigcc.core.common.helper;

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

    public static String join(String delimiter, String ... values){
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            if(isBlank(value)){
                continue;
            }
            if(builder.length()>0){
                builder.append(delimiter);
            }
            builder.append(value);
        }
        return builder.toString();
    }

}
