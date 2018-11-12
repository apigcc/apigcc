package com.apigcc.example.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
