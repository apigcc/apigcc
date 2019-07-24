package com.apigcc.common.resolver;

import com.apigcc.common.description.ArrayTypeDescription;
import com.apigcc.common.description.PrimitiveTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

public class ArrayTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return type.isArray();
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new ArrayTypeDescription(TypeResolvers.resolve(type.asArrayType().getComponentType()));
    }
}
