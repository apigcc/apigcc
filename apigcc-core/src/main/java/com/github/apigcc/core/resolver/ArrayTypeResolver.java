package com.github.apigcc.core.resolver;

import com.github.apigcc.core.Apigcc;
import com.github.apigcc.core.description.ArrayTypeDescription;
import com.github.apigcc.core.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;

public class ArrayTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return type.isArray();
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new ArrayTypeDescription(Apigcc.getInstance().getTypeResolvers().resolve(type.asArrayType().getComponentType()));
    }

}
