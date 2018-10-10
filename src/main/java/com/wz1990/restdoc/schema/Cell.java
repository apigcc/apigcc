package com.wz1990.restdoc.schema;

import lombok.Getter;
import lombok.Setter;

/**
 * 域
 * 请求参数、返回参数等的描述
 */
@Getter
@Setter
public class Cell {

    /**
     * 域名称
     */
    String key;
    /**
     * 域类型
     */
    String type;
    /**
     * 域值，可以是数字、字符串、复杂类型、数组、集合
     */
    Object value;
    String description;

}
