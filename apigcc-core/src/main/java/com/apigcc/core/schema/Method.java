package com.apigcc.core.schema;

import lombok.extern.slf4j.Slf4j;

/**
 * 支持的http method
 */
@Slf4j
public enum Method {


    GET,POST,PUT,DELETE,
    OPTIONS,PATCH,COPY,HEAD,LINK,UNLINK,PURGE,LOCK,UNLOCK,PROPFIND,VIEW;

    public static Method of(String name) {
        return valueOf(name);
    }

}
