package com.github.ayz6uem.apiggy.ast;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

/**
 * Number类型工具类
 */
public class Langs {

    /**
     * 判断是否是基本数字类型
     * @param resolvedType
     * @return
     */
    public static boolean isAssignableBy(ResolvedType resolvedType){
        if(resolvedType instanceof ReferenceTypeImpl){
            String id = ((ReferenceTypeImpl) resolvedType).getId();
            return id.startsWith("java");
        }
        return false;
    }

}
