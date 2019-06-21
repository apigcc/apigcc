package com.apigcc.spring;

import com.apigcc.common.AnnotationHelper;
import com.apigcc.common.ClassDeclarationHelper;
import com.apigcc.common.OptionalHelper;
import com.apigcc.common.URI;
import com.apigcc.schema.HttpMethod;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequestMappingHelper {


    public static final String ANNOTATION_GET_MAPPING = "GetMapping";
    public static final String ANNOTATION_POST_MAPPING = "PostMapping";
    public static final String ANNOTATION_PUT_MAPPING = "PutMapping";
    public static final String ANNOTATION_PATCH_MAPPING = "PatchMapping";
    public static final String ANNOTATION_DELETE_MAPPING = "DeleteMapping";
    public static final String ANNOTATION_REQUEST_MAPPING = "RequestMapping";

    public static final List<String> ANNOTATION_REQUEST_MAPPINGS = Lists.newArrayList(ANNOTATION_GET_MAPPING,
            ANNOTATION_POST_MAPPING, ANNOTATION_PUT_MAPPING, ANNOTATION_PATCH_MAPPING, ANNOTATION_DELETE_MAPPING, ANNOTATION_REQUEST_MAPPING);


    public static String pickMethod(MethodDeclaration n){
        if(n.isAnnotationPresent(ANNOTATION_GET_MAPPING)){
            return HttpMethod.GET;
        }
        if(n.isAnnotationPresent(ANNOTATION_POST_MAPPING)){
            return HttpMethod.POST;
        }
        if(n.isAnnotationPresent(ANNOTATION_PUT_MAPPING)){
            return HttpMethod.PUT;
        }
        if(n.isAnnotationPresent(ANNOTATION_PATCH_MAPPING)){
            return HttpMethod.PATCH;
        }
        if(n.isAnnotationPresent(ANNOTATION_DELETE_MAPPING)){
            return HttpMethod.DELETE;
        }
        if(n.isAnnotationPresent(ANNOTATION_REQUEST_MAPPING)){
            AnnotationExpr annotationExpr = n.getAnnotationByName(ANNOTATION_REQUEST_MAPPING).get();
            Optional<Expression> expressionOptional = AnnotationHelper.getAttribute(annotationExpr, "method");
            if (expressionOptional.isPresent()) {
                Expression expression = expressionOptional.get();
                if(expression.isArrayInitializerExpr()){
                    return expression.asArrayInitializerExpr().getValues()
                            .stream().map(expr -> Objects.toString(expr).replaceAll("RequestMethod.","")).collect(Collectors.joining(","));
                }
                return expression.toString().replaceAll("RequestMethod.","");
            }
        }
        return HttpMethod.GET;
    }


    /**
     * 获取uri数据
     * @param n
     * @return
     */
    public static URI pickUriToParent(ClassOrInterfaceDeclaration n){
        URI parentUri = null;
        Optional<ClassOrInterfaceDeclaration> parentOptional = ClassDeclarationHelper.getParent(n);
        if (parentOptional.isPresent()) {
            parentUri = pickUriToParent(parentOptional.get());
        }
        URI uri = new URI(pickUri(n.getAnnotations()));
        if(parentUri!=null){
            parentUri.add(uri);
            return parentUri;
        }
        return uri;
    }

    /**
     * 获取uri数据，有多个时，暂时只取第一个
     * @param nodeList
     * @return
     */
    public static String pickUri(NodeList<AnnotationExpr> nodeList){
        for (AnnotationExpr annotationExpr : nodeList) {
            if(ANNOTATION_REQUEST_MAPPINGS.contains(annotationExpr.getNameAsString())){
                Optional<Expression> expressionOptional = OptionalHelper.any(
                        AnnotationHelper.getAttribute(annotationExpr, "value"),
                        AnnotationHelper.getAttribute(annotationExpr, "path")
                );
                if (expressionOptional.isPresent()) {
                    Expression expression = expressionOptional.get();
                    if(expression.isStringLiteralExpr()){
                        return expression.asStringLiteralExpr().getValue();
                    }else if(expression.isArrayInitializerExpr()){
                        for (Expression e : expression.asArrayInitializerExpr().getValues()) {
                            if(e.isStringLiteralExpr()){
                                return e.asStringLiteralExpr().getValue();
                            }else{
                                return e.toString();
                            }
                        }
                    }else{
                        return expression.toString();
                    }
                }
            }
        }
        return "";
    }

}
