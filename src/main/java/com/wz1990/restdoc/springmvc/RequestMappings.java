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
    public static final String PATCH_MAPPING = "PatchMapping";
    public static final String DELETE_MAPPING = "DeleteMapping";
    public static final String REQUEST_MAPPING = "RequestMapping";

    public static final List<String> ANNOTATIONS = Arrays.asList(GET_MAPPING,
            POST_MAPPING,
            PUT_MAPPING,
            PATCH_MAPPING,
            DELETE_MAPPING,
            REQUEST_MAPPING);

    public static final Map<String,HttpRequestMethod> ANNOTATION_METHOD = new HashMap<>(ANNOTATIONS.size());

    static {
        ANNOTATION_METHOD.put(GET_MAPPING,HttpRequestMethod.GET);
        ANNOTATION_METHOD.put(POST_MAPPING,HttpRequestMethod.POST);
        ANNOTATION_METHOD.put(PUT_MAPPING,HttpRequestMethod.PUT);
        ANNOTATION_METHOD.put(PATCH_MAPPING,HttpRequestMethod.PATCH);
        ANNOTATION_METHOD.put(DELETE_MAPPING,HttpRequestMethod.DELETE);
        ANNOTATION_METHOD.put(REQUEST_MAPPING,HttpRequestMethod.GET);
    }

    public static final String REQUEST_METHOD_GET = "RequestMethod.GET";
    public static final String REQUEST_METHOD_POST = "RequestMethod.POST";
    public static final String REQUEST_METHOD_PUT = "RequestMethod.PUT";
    public static final String REQUEST_METHOD_PATCH = "RequestMethod.PATCH";
    public static final String REQUEST_METHOD_DELETE = "RequestMethod.DELETE";

    public static final Map<String,HttpRequestMethod> ATTRS_METHOD = new HashMap<>();

    static {
        ATTRS_METHOD.put(REQUEST_METHOD_GET,HttpRequestMethod.GET);
        ATTRS_METHOD.put(REQUEST_METHOD_POST,HttpRequestMethod.POST);
        ATTRS_METHOD.put(REQUEST_METHOD_PUT,HttpRequestMethod.PUT);
        ATTRS_METHOD.put(REQUEST_METHOD_PATCH,HttpRequestMethod.PATCH);
        ATTRS_METHOD.put(REQUEST_METHOD_DELETE,HttpRequestMethod.DELETE);
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
    HttpHeaders headers = new HttpHeaders();

    public static RequestMappings of(AnnotationExpr n){
        if(!accept(n)){
            throw new IllegalArgumentException("annotationExpr not accept:"+n.getNameAsString());
        }
        //解析注解各个属性
        Map<String,Object> annotationAttrs = AstUtils.parseAtts(n);

        RequestMappings requestMappings = new RequestMappings();
        //解析并设置http请求方法
        if(annotationAttrs.containsKey("method")){
            HttpRequestMethod m = ATTRS_METHOD.get(String.valueOf(annotationAttrs.get("method")));
            if(m!=null){
                requestMappings.setMethod(m);
            }
        }else{
            requestMappings.setMethod(ANNOTATION_METHOD.get(n.getNameAsString()));
        }
        //解析并设置http请求路径
        if(annotationAttrs.containsKey("value")){
            requestMappings.setPath(String.valueOf(annotationAttrs.get("value")));
        }else{
            requestMappings.setPath("");
        }
        //TODO 解析headers


        return requestMappings;
    }

    private RequestMappings(){}
}
