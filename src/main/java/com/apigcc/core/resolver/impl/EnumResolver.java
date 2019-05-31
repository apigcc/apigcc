package com.apigcc.core.resolver.impl;

import com.apigcc.core.resolver.Types;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.utils.Pair;

import java.util.List;

public class EnumResolver extends ReferenceResolver {

    @Override
    public boolean accept(ResolvedReferenceTypeDeclaration typeDeclaration) {
        return typeDeclaration.isEnum();
    }

    @Override
    public void resolve(Types types, ResolvedReferenceTypeDeclaration typeDeclaration,
                        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        types.setPrimitive(true);
        types.setValue("");
    }
}
