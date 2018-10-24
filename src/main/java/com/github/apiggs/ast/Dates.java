package com.github.apiggs.ast;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.google.common.collect.Sets;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Number类型工具类
 */
public class Dates {

    public static final Set<String> IDS = Sets.newHashSet(
            LocalDateTime.class.getName(),
            Date.class.getName()
            );

    /**
     * 判断是否是基本数字类型
     * @param typeDeclaration
     * @return
     */
    public static boolean isAssignableBy(ResolvedReferenceTypeDeclaration typeDeclaration){
        return IDS.contains(typeDeclaration.getId());
    }

}
