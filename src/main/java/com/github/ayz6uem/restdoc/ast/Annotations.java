package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            attrs.put("value", Expressions.getValue(singleMemberAnnotationExpr.getMemberValue()));
        }else if(n instanceof NormalAnnotationExpr){
            NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) (n);
            normalAnnotationExpr.getPairs().forEach(ne -> attrs.put(ne.getNameAsString(), Expressions.getValue(ne.getValue())));
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
