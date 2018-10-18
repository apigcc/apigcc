package com.github.apiggs.http;

import java.util.LinkedHashMap;

/**
 * http请求头集合
 */
public class HttpHeaders extends LinkedHashMap<String, String> {

    /**
     * Standard HTTP header names.
     */
    public static final class Names {
        public static final String AUTHORIZATION = "Authorization";
        public static final String CONTENT_ENCODING = "Content-Encoding";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String COOKIE = "Cookie";
        public static final String DATE = "Date";
        public static final String HOST = "Host";
        public static final String SET_COOKIE = "Set-Cookie";
    }

    /**
     * Standard HTTP header values.
     */
    public static final class ContentType {

        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";

        private static final int APPLICATION_JSON_LEVEL = 3;
        private static final int APPLICATION_X_WWW_FORM_URLENCODED_LEVEL = 2;
        private static final int MULTIPART_FORM_DATA_LEVEL = 1;

        /**
         * HTTP header content-Type value's level.
         *
         * @param value
         * @return
         */
        private static int level(String value) {
            switch (value) {
                case APPLICATION_JSON:
                    return APPLICATION_JSON_LEVEL;
                case APPLICATION_X_WWW_FORM_URLENCODED:
                    return APPLICATION_X_WWW_FORM_URLENCODED_LEVEL;
                case MULTIPART_FORM_DATA:
                    return MULTIPART_FORM_DATA_LEVEL;
            }
            return 0;
        }

        private static int compare(String value, String other) {
            return level(value) - level(other);
        }

        private static boolean check(String value) {
            return level(value) > 0;
        }
    }

    /**
     * set contentType value
     * depends on level
     *
     * @param value
     * @return the old value on contentType;
     */
    public void setContentType(String value) {
        if (!ContentType.check(value)) {
            throw new IllegalArgumentException("contentType value not illegal:" + value);
        }
        if (!containsKey(Names.CONTENT_TYPE) || ContentType.compare(value, get(Names.CONTENT_TYPE)) > 0) {
            put(Names.CONTENT_TYPE, value);
        }
    }

    public String getContentType(){
        return get(Names.CONTENT_TYPE);
    }


    /**
     * 加入一个header集合
     * @param headers
     */
    public void add(HttpHeaders headers) {
        if(headers == null || headers.isEmpty()){
            return;
        }
        headers.forEach(this::put);
    }

}
