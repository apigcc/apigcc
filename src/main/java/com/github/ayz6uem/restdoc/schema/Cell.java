package com.github.ayz6uem.restdoc.schema;

import com.github.ayz6uem.restdoc.ast.AstUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        this(name, type, AstUtils.defaultValue(type));
    }

    public Cell(String name, String type, boolean disabled){
        this(name, type, AstUtils.defaultValue(type));
        this.disabled = disabled;
    }

}
