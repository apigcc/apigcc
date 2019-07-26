package com.apigcc.spring;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.Optional;

public class ParameterHelper {

    public static final String ANNOTATION_REQUEST_PARAM = "RequestParam";
    public static final String ANNOTATION_REQUEST_HEADER = "RequestHeader";
    public static final String ANNOTATION_REQUEST_ATTRIBUTE = "RequestAttribute";
    public static final String ANNOTATION_REQUEST_PART = "RequestPart";
    public static final String ANNOTATION_COOKIE_VALUE = "CookieValue";
    public static final String ANNOTATION_PATH_VARIABLE = "PathVariable";
    public static final String ANNOTATION_REQUEST_BODY = "RequestBody";
    public static final String ANNOTATION_MULTIPART_FILE = "MultipartFile";

    public static boolean hasRequestBody(NodeList<Parameter> parameters) {
        for (Parameter parameter : parameters) {
            if (isRequestBody(parameter)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRequestParam(Parameter parameter) {
        if (!parameter.isAnnotationPresent(ANNOTATION_PATH_VARIABLE) &&
                !parameter.isAnnotationPresent(ANNOTATION_REQUEST_BODY) &&
                !parameter.isAnnotationPresent(ANNOTATION_REQUEST_HEADER) &&
                !parameter.isAnnotationPresent(ANNOTATION_COOKIE_VALUE) &&
                !parameter.isAnnotationPresent(ANNOTATION_REQUEST_PART) &&
                !parameter.isAnnotationPresent(ANNOTATION_MULTIPART_FILE) &&
                !parameter.isAnnotationPresent(ANNOTATION_REQUEST_ATTRIBUTE)) {
            return true;
        }
        return false;
    }

    public static boolean isPathVariable(Parameter parameter) {
        if (parameter.isAnnotationPresent(ANNOTATION_PATH_VARIABLE)) {
            return true;
        }
        return false;
    }

    public static boolean isRequestBody(Parameter parameter) {
        if (parameter.isAnnotationPresent(ANNOTATION_REQUEST_BODY)) {
            return true;
        }
        return false;
    }

    public static boolean isRequestHeader(Parameter parameter) {
        if (parameter.isAnnotationPresent(ANNOTATION_REQUEST_HEADER)) {
            return true;
        }
        return false;
    }

}
