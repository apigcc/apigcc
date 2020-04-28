package com.github.apigcc.core.common.helper;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 注解解析
 */
public class AnnotationHelper {

    /**
     * 是否存在任意一个注解
     * @param node
     * @param annotationNames
     * @return
     */
    public static boolean any(NodeWithAnnotations node, Iterable<String> annotationNames) {
        for (String annotationName : annotationNames) {
            if (node.isAnnotationPresent(annotationName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取注解的属性
     * @param expr
     * @param keys 从前往后找，返回第一个存在的属性
     * @return
     */
    public static Optional<Expression> attr(AnnotationExpr expr, String ... keys) {
        for (String key : keys) {
            if (Objects.equals("value", key) && expr.isSingleMemberAnnotationExpr()) {
                return Optional.of(expr.asSingleMemberAnnotationExpr().getMemberValue());
            }
            if (expr.isNormalAnnotationExpr()) {
                for (MemberValuePair pair : expr.asNormalAnnotationExpr().getPairs()) {
                    if (Objects.equals(key, pair.getNameAsString())) {
                        return Optional.of(pair.getValue());
                    }
                }
            }
        }
        return Optional.empty();
    }


    /**
     * 获取注解的属性String类型的值
     * @param node
     * @param annotation
     * @param keys
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Optional<String> string(NodeWithAnnotations node, String annotation, String ... keys) {
        Optional<AnnotationExpr> optional = node.getAnnotationByName(annotation);
        if (optional.isPresent()) {
            Optional<Expression> expr = AnnotationHelper.attr(optional.get(), keys);
            if (expr.isPresent()) {
                return Optional.of(ExpressionHelper.getStringValue(expr.get()));
            }
        }
        return Optional.empty();
    }

}
