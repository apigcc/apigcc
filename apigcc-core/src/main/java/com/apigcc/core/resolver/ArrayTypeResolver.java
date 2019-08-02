package com.apigcc.core.resolver;

import com.apigcc.core.Apigcc;
import com.apigcc.core.common.description.ArrayTypeDescription;
import com.apigcc.core.common.description.TypeDescription;
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
