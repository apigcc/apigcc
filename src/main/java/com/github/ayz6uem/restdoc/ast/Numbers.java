package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Number类型工具类
 */
public class Numbers {

    public static final Set<String> IDS = Sets.newHashSet(
            Byte.class.getName(),
            Short.class.getName(),
            Integer.class.getName(),
            Long.class.getName(),
            Float.class.getName(),
            Double.class.getName(),
            BigDecimal.class.getName(),
            BigInteger.class.getName(),
            AtomicInteger.class.getName(),
            Number.class.getName()
            );

    /**
     * 判断是否是基本数字类型
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
