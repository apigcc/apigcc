package com.wz1990.restdoc.common;

import lombok.Data;

@Data
public class Page {

    /**
     * 第几页
     */
    int page;
    /* 每页条数 */
    int sizs;
    //关键字
    String key;

}
