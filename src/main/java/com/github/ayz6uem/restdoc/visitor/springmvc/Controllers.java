package com.github.ayz6uem.restdoc.visitor.springmvc;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.Arrays;
import java.util.List;

/**
 * Spring @Controller 解析工具
 */
public class Controllers {

    public static final String CONTROLLER = "Controller";
    public static final String REST_CONTROLLER = "RestController";
    public static final String RESPONSE_BODY = "ResponseBody";

    public static final List<String> ANNOTATIONS = Arrays.asList(CONTROLLER,REST_CONTROLLER);

    public static boolean accept(NodeList<AnnotationExpr> nodes){
        for (int i = 0; i < nodes.size(); i++) {
            if(accept(nodes.get(i))){
                return true;
            }
        }
        return false;
    }

    public static boolean accept(AnnotationExpr n){
        if(!ANNOTATIONS.contains(n.getNameAsString())){
            return false;
        }
        return true;
    }
    
    public static boolean isResponseBody(ClassOrInterfaceDeclaration n){
        return n.isAnnotationPresent(REST_CONTROLLER) || n.isAnnotationPresent(RESPONSE_BODY);
    }

    private Controllers(){}
}
