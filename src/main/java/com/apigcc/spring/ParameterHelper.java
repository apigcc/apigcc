package com.apigcc.spring;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;

public class ParameterHelper {

    public static final String ANNOTATION_REQUEST_PARAM = "RequestParam";
    public static final String ANNOTATION_PATH_VARIABLE = "PathVariable";
    public static final String ANNOTATION_REQUEST_BODY = "RequestBody";

    public static boolean hasRequestBody(NodeList<Parameter> parameters) {
        for (Parameter parameter : parameters) {
            if (isRequestBody(parameter)) {
                return true;
            }
        }
        return false;
    }

    public static Parameter getRequestBody(NodeList<Parameter> parameters){
        for (Parameter parameter : parameters) {
            if (isRequestBody(parameter)) {
                return parameter;
            }
        }
        return null;
    }

    public static boolean isRequestParam(Parameter parameter) {
        if (!parameter.isAnnotationPresent(ANNOTATION_PATH_VARIABLE) &&
                !parameter.isAnnotationPresent(ANNOTATION_REQUEST_BODY)) {
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

}
