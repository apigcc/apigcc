package com.github.ayz6uem.restdoc.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * 域
 * 请求参数、返回参数等的描述
 */
@Getter
@Setter
@NoArgsConstructor
public class Cell {

    /**
     * 域名称
     */
    String name;
    /**
     * 域类型
     */
    String type;
    /**
     * 域值，可以是数字、字符串、复杂类型、数组、集合
     */
    Object value;
    String description;
    boolean disabled;

    public Cell(String name, String type, Object value){
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Cell(String name, String type){
        this(name, type, null);
    }

    public Cell(String name, String type, boolean disabled){
        this(name, type);
        this.disabled = disabled;
    }

    public List<String> toList() {
        return Arrays.asList(name, type, value==null?"":String.valueOf(value), description);
    }
}
