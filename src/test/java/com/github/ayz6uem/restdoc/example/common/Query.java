package com.github.ayz6uem.restdoc.example.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query {

    /**
     * static will be ignore
     */
    public static final String CONSTANS = "";

    /**
     * 查询关键字
     */
    String q = "";

}
