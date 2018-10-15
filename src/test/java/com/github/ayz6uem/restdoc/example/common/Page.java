package com.github.ayz6uem.restdoc.example.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Page extends Query {

    /**
     * 第几页
     */
    int page = 1;
    /* 每页条数 */
    int sizs = 20;

}
