package com.apigcc.core.resolver;

import com.apigcc.core.common.description.TypeDescription;
import com.apigcc.core.common.description.UnAvailableTypeDescription;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedType;

public interface TypeResolver {

    boolean accept(ResolvedType type);

    TypeDescription resolve(ResolvedType type);

}
