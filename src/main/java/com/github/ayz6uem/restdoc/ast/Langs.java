package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.google.common.collect.Sets;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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
