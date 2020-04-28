package com.github.apigcc.core.common.helper;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.Optional;

/**
 * 被Json序列化框架注解的字段
 * 解析并获取序列化时的别名
 */
public class JsonPropertyHelper {

    private static final String ANNOTATION_JSON_PROPERTY = "JsonProperty";
    private static final String ANNOTATION_JSON_FIELD = "JSONField";
    private static final String ANNOTATION_SERIALIZED_NAME = "SerializedName";

    /**
     * 获取Json 别名
     *
     * @param declaredField
     * @return
     */
    public static Optional<String> getJsonName(ResolvedFieldDeclaration declaredField) {
        if (declaredField instanceof JavaParserFieldDeclaration) {
            FieldDeclaration fieldDeclaration = ((JavaParserFieldDeclaration) declaredField).getWrappedNode();
            return OptionalHelper.any(
                    AnnotationHelper.string(fieldDeclaration, ANNOTATION_JSON_PROPERTY, "value"),
                    AnnotationHelper.string(fieldDeclaration, ANNOTATION_JSON_FIELD, "name"),
                    AnnotationHelper.string(fieldDeclaration, ANNOTATION_SERIALIZED_NAME, "value")
            );
        }
        return Optional.empty();
    }

}
