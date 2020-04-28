package com.github.apigcc.core.resolver;

import com.github.apigcc.core.Apigcc;
import com.github.javaparser.resolution.types.ResolvedType;

public abstract class ReferenceTypeResolver implements TypeResolver {

    @Override
    public boolean accept(ResolvedType type) {
        return type.isReferenceType() &&
                !Apigcc.getInstance().getContext().hasCodeTypeDeclaration(type.asReferenceType().getId());
    }
}
