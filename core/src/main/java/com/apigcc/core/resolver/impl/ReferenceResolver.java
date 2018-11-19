package com.apigcc.core.resolver.impl;

import com.apigcc.core.resolver.Types;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.utils.Pair;

import java.util.List;

public abstract class ReferenceResolver extends Resolver {
    @Override
    public boolean accept(ResolvedType resolvedType) {
        return super.accept(resolvedType) && resolvedType.isReferenceType() && accept(resolvedType.asReferenceType().getTypeDeclaration());
    }

    @Override
    public void resolve(Types types, ResolvedType resolvedType) {
        ResolvedReferenceType referenceType = resolvedType.asReferenceType();
        ResolvedReferenceTypeDeclaration typeDeclaration = referenceType.getTypeDeclaration();
        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap = referenceType.getTypeParametersMap();
        resolve(types, typeDeclaration, typeParametersMap);
    }

    public abstract boolean accept(ResolvedReferenceTypeDeclaration typeDeclaration);

    public abstract void resolve(Types types, ResolvedReferenceTypeDeclaration typeDeclaration,
                                 List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap);
}
