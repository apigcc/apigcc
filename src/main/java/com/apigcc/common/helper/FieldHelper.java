package com.apigcc.common.helper;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.Optional;

public class FieldHelper {

    /**
     * 通过access方法，获取属性名
     * @param methodName access方法名
     * @return 属性名
     */
    public static String getByAccessMethod(String methodName){
        if(methodName.startsWith("is") && methodName.length()>2){
            String first = methodName.substring(2, 3);
            String less = methodName.substring(3);
            return first.toLowerCase() + less;
        }
        if(methodName.startsWith("get") && methodName.length()>3){
            String first = methodName.substring(3, 4);
            String less = methodName.substring(4);
            return first.toLowerCase() + less;
        }
        return null;
    }

    public static Optional<Expression> getInitializer(ResolvedFieldDeclaration declaredField){
        if(declaredField instanceof JavaParserFieldDeclaration){
            JavaParserFieldDeclaration field = (JavaParserFieldDeclaration) declaredField;
            return field.getVariableDeclarator().getInitializer();
        }
        return Optional.empty();
    }

}
