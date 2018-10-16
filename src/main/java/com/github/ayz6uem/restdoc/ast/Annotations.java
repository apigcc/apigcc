package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.expr.*;
import com.google.common.collect.Sets;

import java.util.*;

public class Annotations {

    /**
     * 获取注解表达式中，各个属性的值
     * @param n
     * @return
     */
    public static Map<String,Object> parseAtts(AnnotationExpr n){
        Map<String,Object> attrs = new HashMap<>();
        if(n instanceof SingleMemberAnnotationExpr){
            SingleMemberAnnotationExpr singleMemberAnnotationExpr = (SingleMemberAnnotationExpr) n;
            if(singleMemberAnnotationExpr.getMemberValue() instanceof StringLiteralExpr){
                StringLiteralExpr stringLiteralExpr = (StringLiteralExpr) singleMemberAnnotationExpr.getMemberValue();
                attrs.put("value",stringLiteralExpr.asString());
            }
        }else if(n instanceof NormalAnnotationExpr){
            NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) (n);
            normalAnnotationExpr.getPairs().forEach(ne -> {
                if(ne.getValue() instanceof StringLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((StringLiteralExpr)ne.getValue()).getValue());
                }else if(ne.getValue() instanceof IntegerLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((IntegerLiteralExpr)ne.getValue()).getValue());
                }else if(ne.getValue() instanceof DoubleLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((DoubleLiteralExpr)ne.getValue()).getValue());
                }else if(ne.getValue() instanceof LongLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((LongLiteralExpr)ne.getValue()).getValue());
                }else if(ne.getValue() instanceof BooleanLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((BooleanLiteralExpr)ne.getValue()).getValue());
                }else{
                    attrs.put(ne.getNameAsString(), ne.getValue().toString());
                }
            });
        }
        return attrs;
    }

    /**
     * 获取注解的某个属性
     * @param annotationExpr
     * @param key
     * @return
     */
    public static Object getAttr(Optional<AnnotationExpr> annotationExpr, String key) {
        if(annotationExpr.isPresent()){
            return parseAtts(annotationExpr.get()).get(key);
        }
        return null;
    }

}
