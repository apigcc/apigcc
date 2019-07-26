package com.apigcc.common.helper;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public static String getStringValue(Expression expression){
        if(expression.isStringLiteralExpr()){
            return expression.asStringLiteralExpr().getValue();
        }else if(expression.isArrayInitializerExpr()){
            NodeList<Expression> values = expression.asArrayInitializerExpr().getValues();
            if(values.size()>0){
                return getStringValue(values.get(0));
            }
        }else if(expression instanceof Resolvable){
            return String.valueOf(resolve((Resolvable)expression));
        }
        return expression.toString();
    }

    public static List<String> getStringValues(Expression expression){
        List<String> results = new ArrayList<>();
        if(expression.isStringLiteralExpr()){
            results.add(expression.asStringLiteralExpr().getValue());
        }else if(expression.isArrayInitializerExpr()) {
            NodeList<Expression> values = expression.asArrayInitializerExpr().getValues();
            for (Expression value : values) {
                results.addAll(getStringValues(value));
            }
        }else if(expression instanceof Resolvable){
            results.add(String.valueOf(resolve((Resolvable)expression)));
        }else{
            results.add(expression.toString());
        }
        return results;
    }

    private static Object resolve(Resolvable resolvable){
        try {
            Object resolve = resolvable.resolve();
            if(resolve instanceof JavaParserFieldDeclaration){
                Optional<Expression> initializer = ((JavaParserFieldDeclaration) resolve).getVariableDeclarator().getInitializer();
                if (initializer.isPresent()) {
                    return getStringValue(initializer.get());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resolvable;
    }


}
