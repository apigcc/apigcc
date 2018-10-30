package com.github.apiggs.ast;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.Map;
import java.util.Optional;

public class Fields {

    public static final String ANNOTATION_ALIAS_JACKSON = "JsonProperty";
    public static final String ANNOTATION_ALIAS_GSON = "SerializedName";
    public static final String ANNOTATION_ALIAS_FASTJSON = "JSONField";

    public static Optional<Object> getInitializer(JavaParserFieldDeclaration field){
        Object value = null;
        Optional<Expression> initializer = field.getVariableDeclarator().getInitializer();
        if(initializer.isPresent()){
            Expression expr = initializer.get();
            value = Expressions.getValue(expr);
        }
        return Optional.ofNullable(value);
    }


    public static String getName(JavaParserFieldDeclaration field) {
        FieldDeclaration declaration = field.getWrappedNode();
        if(declaration.getAnnotationByName(ANNOTATION_ALIAS_JACKSON).isPresent()){
            Object value = Annotations.getAttr(declaration.getAnnotationByName(ANNOTATION_ALIAS_JACKSON),"value");
            if(value!=null){
                return String.valueOf(value);
            }
        }
        if(declaration.getAnnotationByName(ANNOTATION_ALIAS_GSON).isPresent()){
            Object value = Annotations.getAttr(declaration.getAnnotationByName(ANNOTATION_ALIAS_GSON),"value");
            if(value!=null){
                return String.valueOf(value);
            }
        }
        if(declaration.getAnnotationByName(ANNOTATION_ALIAS_FASTJSON).isPresent()){
            Object value = Annotations.getAttr(declaration.getAnnotationByName(ANNOTATION_ALIAS_FASTJSON),"name");
            if(value!=null){
                return String.valueOf(value);
            }
        }
        return field.getName();
    }
}
