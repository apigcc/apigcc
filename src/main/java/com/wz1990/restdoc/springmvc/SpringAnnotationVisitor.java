package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.wz1990.restdoc.ast.AstHelper;
import com.wz1990.restdoc.schema.Group;
import com.wz1990.restdoc.schema.Item;
import com.wz1990.restdoc.schema.Method;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Spring 注解处理器
 */
public class SpringAnnotationVisitor {

    public boolean accept(ClassOrInterfaceDeclaration n) {
        return AstHelper.isAnyPresent(n, SpringMvcConstants.CONTROLLER, SpringMvcConstants.REST_CONTROLLER);
    }

    public boolean accept(MethodDeclaration n) {
        return AstHelper.isAnyPresent(n,
                SpringMvcConstants.GET_MAPPING,
                SpringMvcConstants.POST_MAPPING,
                SpringMvcConstants.PUT_MAPPING,
                SpringMvcConstants.DELETE_MAPPING,
                SpringMvcConstants.REQUEST_MAPPING);
    }

    public void visit(AnnotationExpr expr, Item item) {
        parseAnnotation(expr, item.getRequest());
    }

    public void visit(AnnotationExpr n, Group group) {
        if(Objects.equals(n.getNameAsString(),SpringMvcConstants.REQUEST_MAPPING)){

            if (n instanceof SingleMemberAnnotationExpr) {
                SingleMemberAnnotationExpr singleMemberAnnotationExpr = (SingleMemberAnnotationExpr) n;
                Expression expression = singleMemberAnnotationExpr.getMemberValue();
                if (expression instanceof StringLiteralExpr) {
                    StringLiteralExpr stringLiteralExpr = (StringLiteralExpr) expression;
                    group.setPath(stringLiteralExpr.asString());
                }
            }
            if (n instanceof NormalAnnotationExpr) {
                NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) (n);
                normalAnnotationExpr.getPairs().forEach(ne -> {
                    if (Objects.equals(new SimpleName("value"), ne.getName())) {
                        group.setPath(((StringLiteralExpr) ne.getValue()).asString());
                    }
                });
            }
        }
    }

    private void parseAnnotation(AnnotationExpr n, Item.Request request) {
        if(!isMappingAnnotation(n)){
            return;
        }
        String annotationName = n.getNameAsString();
        Method method = parseAnnotationName(annotationName);
        if (method == null) {
            return;
        }
        request.setMethod(method);
        if (n instanceof SingleMemberAnnotationExpr) {
            SingleMemberAnnotationExpr singleMemberAnnotationExpr = (SingleMemberAnnotationExpr) n;
            Expression expression = singleMemberAnnotationExpr.getMemberValue();
            if (expression instanceof StringLiteralExpr) {
                StringLiteralExpr stringLiteralExpr = (StringLiteralExpr) expression;
                request.getUrl().setPath(stringLiteralExpr.asString());
            }
        }else if (n instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) (n);
            normalAnnotationExpr.getPairs().forEach(ne -> {
                if (Objects.equals(new SimpleName("value"), ne.getName())) {
                    request.getUrl().setPath(((StringLiteralExpr) ne.getValue()).asString());
                } else if (Objects.equals(new SimpleName("method"), ne.getName())) {
                    request.setMethod(parseRequestMethod(ne.getValue().toString()));
                }
            });
        }else if (n instanceof MarkerAnnotationExpr){
            request.getUrl().setPath("");
        }
    }

    public static Method parseRequestMethod(String requestMapping) {
        if (requestMapping != null && requestMapping.startsWith("RequestMethod.")) {
            String[] arr = requestMapping.split("\\.");
            return Method.valueOf(arr[1]);
        }
        return null;
    }

    private boolean isMappingAnnotation(AnnotationExpr expr){
        return SpringMvcConstants.MAPPINGS.contains(expr.getNameAsString());
    }

    private Method parseAnnotationName(String name) {
        if (name == null) {
            return null;
        }
        switch (name){
            case SpringMvcConstants.PUT_MAPPING:
                return Method.PUT;
            case SpringMvcConstants.DELETE_MAPPING:
                return Method.DELETE;
            case SpringMvcConstants.POST_MAPPING:
                return Method.POST;
            case SpringMvcConstants.GET_MAPPING:
            case SpringMvcConstants.REQUEST_MAPPING:
            default:
                return Method.GET;
        }
    }

}
