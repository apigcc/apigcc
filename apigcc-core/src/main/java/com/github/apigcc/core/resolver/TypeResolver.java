package com.github.apigcc.core.resolver;

import com.github.apigcc.core.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;

/**
 * 类型解析
 */
public interface TypeResolver {

    boolean accept(ResolvedType type);

    TypeDescription resolve(ResolvedType type);

}
