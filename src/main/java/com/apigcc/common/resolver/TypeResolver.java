package com.apigcc.common.resolver;

import com.apigcc.common.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;

public interface TypeResolver {

    boolean accept(ResolvedType type);

    TypeDescription resolve(ResolvedType type);

}
