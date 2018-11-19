package com.apigcc.core.resolver.impl;

import com.apigcc.core.resolver.Types;
import com.apigcc.core.resolver.ast.Clazz;
import com.apigcc.core.resolver.ast.Defaults;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.utils.Pair;

import java.util.List;

public class DateResolver extends ReferenceResolver {


    @Override
    public boolean accept(ResolvedReferenceTypeDeclaration typeDeclaration) {
        return Clazz.Dates.isAssignableBy(typeDeclaration);
    }

    @Override
    public void resolve(Types types, ResolvedReferenceTypeDeclaration typeDeclaration,
                        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        types.setValue(Defaults.DEFAULT_STRING);
    }
}
