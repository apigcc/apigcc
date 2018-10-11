package com.github.ayz6uem.restdoc.ast;

import lombok.Data;

/**
 * 语法树泛型
 */
@Data
public class AstGeneric {

    /**
     * 泛型的标识 例如 T
     */
    String symbol;
    /**
     * 泛型的序号
     */
    int index;

}
