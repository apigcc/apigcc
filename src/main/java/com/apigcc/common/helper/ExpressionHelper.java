package com.apigcc.common.helper;

import com.github.javaparser.ast.expr.*;

import java.util.stream.Collectors;

public class ExpressionHelper {

    /**
     * 解析表达式，获取表达式的值
     * TODO 更复杂的表达式解析
     * @param expr
     * @return
     */
    public static Object getValue(Expression expr) {
        if (expr.isStringLiteralExpr()) {
            return expr.asStringLiteralExpr().getValue();
        }
        if (expr.isIntegerLiteralExpr()) {
            return expr.asIntegerLiteralExpr().getValue();
        }
        if (expr.isDoubleLiteralExpr()) {
            return expr.asDoubleLiteralExpr().getValue();
        }
        if (expr.isLongLiteralExpr()) {
            return expr.asLongLiteralExpr().getValue();
        }
        if (expr.isBooleanLiteralExpr()) {
            return expr.asBooleanLiteralExpr().getValue();
        }
        if (expr.isArrayInitializerExpr()) {
            return expr.asArrayInitializerExpr().getValues().stream().map(ExpressionHelper::getValue).collect(Collectors.toList());
        }
        return expr.toString();
    }

}
