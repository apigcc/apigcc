package com.apigcc.core.handler.postman.schema;

import com.apigcc.core.common.markup.asciidoc.Color;
import com.apigcc.core.http.HttpRequestMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * 支持的http method
 */
@Slf4j
public enum Method {


    GET,POST,PUT,DELETE,
    OPTIONS,PATCH,COPY,HEAD,LINK,UNLINK,PURGE,LOCK,UNLOCK,PROPFIND,VIEW;

    public static Method of(HttpRequestMethod method) {
        return valueOf(method.name());
    }

    public static Color getColor(String method){
        try {
            switch (valueOf(method)){
                case GET:
                    return Color.GREEN;
                case POST:
                    return Color.YELLOW;
                case PUT:
                    return Color.BLUE;
                case DELETE:
                    return Color.RED;
                default:
            }
        }catch (Exception e){
            log.debug("parse method error:{}",method);
        }
        return Color.BLACK;
    }

}
