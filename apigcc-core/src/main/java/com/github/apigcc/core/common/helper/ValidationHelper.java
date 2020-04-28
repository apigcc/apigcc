package com.github.apigcc.core.common.helper;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ValidationHelper {

    public static final String NULL = "Null";
    public static final String NOT_NULL = "NotNull";
    public static final String ASSERT_TRUE = "AssertTrue";
    public static final String ASSERT_FALSE = "AssertFalse";
    public static final String NOT_EMPTY = "NotEmpty";
    public static final String NOT_BLANK = "NotBlank";
    public static final String EMAIL = "Email";

    public static final String MIN = "Min";
    public static final String MAX = "Max";
    public static final String SIZE = "Size";

    public static final List<String> values = Lists.newArrayList(NULL, NOT_NULL, NOT_EMPTY, EMAIL, NOT_BLANK);

    public static List<String> getValidations(ResolvedFieldDeclaration declaredField) {
        List<String> result = new ArrayList<>();
        if (declaredField instanceof JavaParserFieldDeclaration) {
            FieldDeclaration fieldDeclaration = ((JavaParserFieldDeclaration) declaredField).getWrappedNode();
            for (String value : values) {
                Optional<AnnotationExpr> optional = fieldDeclaration.getAnnotationByName(value);
                if (optional.isPresent()) {
                    result.add(value);
                }
            }
        }
        return result;
    }

}
