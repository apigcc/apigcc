package com.apigcc.core.common.helper;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.Optional;

public class JsonPropertyHelper {

    public static final String ANNOTAION_JSON_PROPERTY = "JsonProperty";
    public static final String ANNOTAION_JSON_FIELD = "JSONField";
    public static final String ANNOTAION_SERIALIZED_NAME = "SerializedName";

    public static Optional<String> getJsonName(ResolvedFieldDeclaration declaredField){
        if(declaredField instanceof JavaParserFieldDeclaration){
            FieldDeclaration fieldDeclaration = ((JavaParserFieldDeclaration) declaredField).getWrappedNode();
            return OptionalHelper.any(
                    getStringValue(fieldDeclaration, ANNOTAION_JSON_PROPERTY, "value"),
                    getStringValue(fieldDeclaration, ANNOTAION_JSON_FIELD, "name"),
                    getStringValue(fieldDeclaration, ANNOTAION_SERIALIZED_NAME, "value")
            );
        }
        return Optional.empty();
    }

    public static Optional<String> getStringValue(FieldDeclaration fieldDeclaration, String anno, String attr){
        Optional<AnnotationExpr> optional = fieldDeclaration.getAnnotationByName(anno);
        if (optional.isPresent()) {
            Optional<Expression> expr = AnnotationHelper.getAttribute(optional.get(),attr);
            if (expr.isPresent()) {
                return Optional.of(ExpressionHelper.getStringValue(expr.get()));
            }
        }
        return Optional.empty();
    }

}
