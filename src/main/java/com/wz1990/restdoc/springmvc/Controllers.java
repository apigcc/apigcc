package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.wz1990.restdoc.ast.AstUtils;
import com.wz1990.restdoc.http.HttpHeaders;
import com.wz1990.restdoc.http.HttpRequestMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring @Controller 解析工具
 */
@Setter
@Getter
public class Controllers {

    public static final String CONTROLLER = "Controller";
    public static final String REST_CONTROLLER = "RestController";

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

    private Controllers(){}
}
