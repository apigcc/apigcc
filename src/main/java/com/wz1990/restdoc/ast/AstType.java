package com.wz1990.restdoc.ast;

import com.github.javaparser.ast.type.Type;
import lombok.Data;

@Data
public class AstType {

    Type type;
    boolean array;
    String compoent;

}
