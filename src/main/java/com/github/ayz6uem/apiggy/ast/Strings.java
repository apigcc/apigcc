package com.github.ayz6uem.apiggy.ast;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * String类型工具类
 */
public class Strings {

    public static final Set<String> IDS = Sets.newHashSet(
            String.class.getName(),
            Character.class.getName(),
            CharSequence.class.getName()
            );

    /**
     * 判断是否是基本字符类型
     * @param resolvedType
     * @return
     */
    public static boolean isAssignableBy(ResolvedType resolvedType){
        if(resolvedType instanceof ReferenceTypeImpl){
            return IDS.contains(((ReferenceTypeImpl) resolvedType).getId());
        }
        return false;
    }

}
