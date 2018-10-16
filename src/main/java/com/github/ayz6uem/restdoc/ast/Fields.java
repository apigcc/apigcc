package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.Optional;

public class Fields {

    public static Optional<Object> getInitializer(JavaParserFieldDeclaration field){
        Object value = null;
        Optional<Expression> initializer = field.getVariableDeclarator().getInitializer();
        if(initializer.isPresent()){
            Expression expr = initializer.get();
            if(expr.isStringLiteralExpr()){
                value = expr.asStringLiteralExpr().getValue();
            }else{
                value = expr.toString();
            }
        }
        return Optional.ofNullable(value);
    }

}
