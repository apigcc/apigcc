package com.github.apiggs.ast;

import java.util.HashMap;
import java.util.Map;

public class Defaults {

    public static final String DEFAULT_STRING = "";
    public static final Integer DEFAULT_INTEGER = 0;
    public static final Map DEFAULT_MAP = new HashMap();

    public static Object get(String type){
        switch (type){
            case "short":
            case "java.lang.Short":
            case "int":
            case "java.lang.Integer":
            case "long":
            case "java.lang.Long":
                return DEFAULT_INTEGER;
            case "String":
            case "java.lang.String":
            default:
                return DEFAULT_STRING;
        }

    }

}
