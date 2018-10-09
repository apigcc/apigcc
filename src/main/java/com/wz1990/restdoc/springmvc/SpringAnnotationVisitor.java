package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.wz1990.restdoc.ast.AstHelper;
import com.wz1990.restdoc.schema.Item;
import com.wz1990.restdoc.schema.Method;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Spring 注解处理器
 */
public class SpringAnnotationVisitor {

    public static final String CONTROLLER = "Controller";
    public static final String REST_CONTROLLER = "RestController";

    public static final String GET_MAPPING = "GetMapping";
    public static final String POST_MAPPING = "PostMapping";
    public static final String PUT_MAPPING = "PutMapping";
    public static final String DELETE_MAPPING = "DeleteMapping";
    public static final String REQUEST_MAPPING = "RequestMapping";

    public static final List<String> MAPPINGS = Arrays.asList(GET_MAPPING,POST_MAPPING,PUT_MAPPING,DELETE_MAPPING,REQUEST_MAPPING);

    public boolean accept(ClassOrInterfaceDeclaration n) {
        return AstHelper.isAnyPresent(n, CONTROLLER, REST_CONTROLLER);
    }

    public boolean accept(MethodDeclaration n) {
        return AstHelper.isAnyPresent(n, GET_MAPPING, POST_MAPPING, PUT_MAPPING, DELETE_MAPPING, REQUEST_MAPPING);
    }

    public void visit(AnnotationExpr expr, Item item) {
        parseAnnotation(expr, item.getRequest());
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
        }
        if (n instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) (n);
            normalAnnotationExpr.getPairs().forEach(ne -> {
                if (Objects.equals(new SimpleName("value"), ne.getName())) {
                    request.getUrl().setPath(((StringLiteralExpr) ne.getValue()).asString());
                } else if (Objects.equals(new SimpleName("method"), ne.getName())) {
                    request.setMethod(parseRequestMethod(ne.getValue().toString()));
                }
            });
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
        return MAPPINGS.contains(expr.getNameAsString());
    }

    private Method parseAnnotationName(String name) {
        if (name == null) {
            return null;
        }
        switch (name){
            case PUT_MAPPING:
                return Method.PUT;
            case DELETE_MAPPING:
                return Method.DELETE;
            case POST_MAPPING:
                return Method.POST;
            case GET_MAPPING:
            case REQUEST_MAPPING:
            default:
                return Method.GET;
        }
    }

}
