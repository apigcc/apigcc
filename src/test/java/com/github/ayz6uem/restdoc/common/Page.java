package com.github.ayz6uem.restdoc.common;

import lombok.Data;

@Data
public class Page extends Query {

    /**
     * 第几页
     */
    int page;
    /* 每页条数 */
    int sizs;
    //关键字
    String key;

}
