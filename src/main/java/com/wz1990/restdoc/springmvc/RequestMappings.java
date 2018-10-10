package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.wz1990.restdoc.ast.AstUtils;
import com.wz1990.restdoc.http.HttpHeaders;
import com.wz1990.restdoc.http.HttpRequestMethod;
import jnr.ffi.util.Annotations;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Spring @RequestMapping 解析工具
 */
@Setter
@Getter
public class RequestMappings {

    public static final String GET_MAPPING = "GetMapping";
    public static final String POST_MAPPING = "PostMapping";
    public static final String PUT_MAPPING = "PutMapping";
    public static final String DELETE_MAPPING = "DeleteMapping";
    public static final String REQUEST_MAPPING = "RequestMapping";

    public static final List<String> ANNOTATIONS = Arrays.asList(GET_MAPPING,POST_MAPPING,PUT_MAPPING,DELETE_MAPPING,REQUEST_MAPPING);

    public static final Map<String,HttpRequestMethod> ANNOTATION_METHOD = new HashMap<>(ANNOTATIONS.size());

    static {
        ANNOTATION_METHOD.put(GET_MAPPING,HttpRequestMethod.GET);
        ANNOTATION_METHOD.put(POST_MAPPING,HttpRequestMethod.POST);
        ANNOTATION_METHOD.put(PUT_MAPPING,HttpRequestMethod.PUT);
        ANNOTATION_METHOD.put(DELETE_MAPPING,HttpRequestMethod.DELETE);
        ANNOTATION_METHOD.put(REQUEST_MAPPING,HttpRequestMethod.GET);
    }

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

    HttpRequestMethod method;
    String path;
    HttpHeaders headers;

    public static RequestMappings of(AnnotationExpr n){
        if(!accept(n)){
            throw new IllegalArgumentException("annotationExpr not accept:"+n.getNameAsString());
        }

        Map<String,Object> annotationAttrs = AstUtils.parseAtts(n);

        RequestMappings requestMappings = new RequestMappings();
        requestMappings.setMethod(ANNOTATION_METHOD.get(n.getNameAsString()));
        if(annotationAttrs.containsKey("value")){
            requestMappings.setPath(String.valueOf(annotationAttrs.get("value")));
        }else{
            requestMappings.setPath("");
        }
        return requestMappings;
    }

    private RequestMappings(){}
}
