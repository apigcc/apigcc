package com.apigcc.core.common.description;

import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;


public class PrimitiveTypeDescription extends TypeDescription {

    public PrimitiveTypeDescription(ResolvedReferenceType referenceType){
        switch (referenceType.getId()){
            case "java.lang.Byte":
                value = (byte) 0;
                type = "byte";
                break;
            case "java.lang.Short":
                value = (short) 0;
                type = "short";
                break;
            case "java.lang.Integer":
                value = 0;
                type = "int";
                break;
            case "java.lang.Long":
                value = 0L;
                type = "long";
                break;
            case "java.lang.Float":
                value = 0f;
                type = "float";
                break;
            case "java.lang.Double":
                value = 0d;
                type = "double";
                break;
            case "java.lang.Character":
                value = (char)0;
                type = "char";
                break;
            case "java.lang.Boolean":
                value = false;
                type = "boolean";
                break;
        }

    }
    public PrimitiveTypeDescription(ResolvedPrimitiveType resolvedPrimitiveType){
        type = resolvedPrimitiveType.describe();
        switch (resolvedPrimitiveType){
            case BYTE:
                value = (byte) 0;
                break;
            case SHORT:
                value = (short) 0;
                break;
            case INT:
                value = 0;
                break;
            case LONG:
                value = 0L;
                break;
            case FLOAT:
                value = 0f;
                break;
            case DOUBLE:
                value = 0d;
                break;
            case CHAR:
                value = (char)0;
                break;
            case BOOLEAN:
                value = false;
                break;
        }
    }
}
