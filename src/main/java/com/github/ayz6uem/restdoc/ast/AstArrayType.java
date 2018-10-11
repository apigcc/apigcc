package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.type.Type;
import lombok.Data;

/**
 * 语法树类型
 */
@Data
public class AstArrayType {

    Type type;
    /**
     * 是否为集合
     */
    boolean array;
    /**
     * 组件名称
     * 或
     * 集合中所存的组件名称
     */
    String compoent;

}
