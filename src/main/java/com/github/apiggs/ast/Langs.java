package com.github.apiggs.ast;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;

/**
 * Number类型工具类
 */
public class Langs {

    /**
     * 判断是否是基本数字类型
     * @param typeDeclaration
     * @return
     */
    public static boolean isAssignableBy(ResolvedReferenceTypeDeclaration typeDeclaration){
        String id = typeDeclaration.getId();
        return id.startsWith("java");
    }

}
