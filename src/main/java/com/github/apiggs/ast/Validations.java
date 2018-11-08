package com.github.apiggs.ast;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * 解析javax.validation
 * JSR303
 */
public class Validations {

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

    public static final Set<String> Names = Sets.newHashSet(NULL, NOT_BLANK, NOT_EMPTY,
            NOT_NULL, ASSERT_TRUE, ASSERT_FALSE,
            MIN, MAX, SIZE, EMAIL);

    public static boolean isTarget(AnnotationExpr expr) {
        return Names.contains(expr.getNameAsString());
    }

    public static Validations of(NodeList<AnnotationExpr> list) {
        Validations validations = new Validations();
        for (AnnotationExpr annotationExpr : list) {
            if (isTarget(annotationExpr)) {
                validations.add(annotationExpr);
            }
        }
        return validations;
    }

    private StringBuilder results = new StringBuilder();

    private void add(AnnotationExpr expr) {
        results.append(expr.getNameAsString());
        Map<String, Object> attrs = Annotations.getAttrs(expr);
        if(SIZE.equals(expr.getNameAsString())){
            results.append("(");
            if(attrs.containsKey("min")){
                results.append("min=").append(attrs.get("min"));
            }
            if(attrs.containsKey("min") && attrs.containsKey("max")){
                results.append(",");
            }
            if(attrs.containsKey("max")){
                results.append("max=").append(attrs.get("max"));
            }
            results.append(")");
        }else if(attrs.containsKey("value")){
            results.append("(").append(attrs.get("value")).append(")");
        }
        results.append(" ");
    }

    public String getResults() {
        return results.toString();
    }
}
