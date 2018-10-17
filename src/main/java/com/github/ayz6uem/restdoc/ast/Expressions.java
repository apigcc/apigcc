package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.expr.*;

import java.util.ArrayList;
import java.util.List;

public class Expressions {

    public static Object getValue(Expression expr){
        if(expr instanceof StringLiteralExpr){
            return ((StringLiteralExpr)expr).getValue();
        }else if(expr instanceof IntegerLiteralExpr){
            return ((IntegerLiteralExpr)expr).getValue();
        }else if(expr instanceof DoubleLiteralExpr){
            return ((DoubleLiteralExpr)expr).getValue();
        }else if(expr instanceof LongLiteralExpr){
            return ((LongLiteralExpr)expr).getValue();
        }else if(expr instanceof BooleanLiteralExpr){
            return ((BooleanLiteralExpr)expr).getValue();
        }else if(expr instanceof ArrayInitializerExpr){
            ArrayInitializerExpr array = (ArrayInitializerExpr) expr;
            List list = new ArrayList();
            for (Expression expression : array.getValues()) {
                list.add(getValue(expression));
            }
            return list;
        }
        return expr.toString();
    }

}
