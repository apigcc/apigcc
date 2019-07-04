package com.apigcc.common.helper;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AnnotationHelper {

    public static boolean isAnnotationPresent(NodeWithAnnotations node, List<String> annotationNames) {
        for (String annotationName : annotationNames) {
            if (node.isAnnotationPresent(annotationName)) {
                return true;
            }
        }
        return false;
    }

    public static Optional<Expression> getAttribute(AnnotationExpr annotationExpr, String key) {
        if (Objects.equals("value", key) && annotationExpr.isSingleMemberAnnotationExpr()) {
            return Optional.of(annotationExpr.asSingleMemberAnnotationExpr().getMemberValue());
        }
        if (annotationExpr.isNormalAnnotationExpr()) {
            for (MemberValuePair pair : annotationExpr.asNormalAnnotationExpr().getPairs()) {
                if (Objects.equals(key, pair.getNameAsString())){
                    return Optional.of(pair.getValue());
                }
            }
        }
        return Optional.empty();
    }

}
