package com.apigcc.core.resolver.impl;

import com.apigcc.core.common.ObjectMappers;
import com.apigcc.core.resolver.TypeResolvers;
import com.apigcc.core.resolver.Types;
import com.apigcc.core.resolver.ast.Clazz;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.utils.Pair;

import java.util.List;

public class CollectionResolver extends ReferenceResolver {


    @Override
    public boolean accept(ResolvedReferenceTypeDeclaration typeDeclaration) {
        return Clazz.Collections.isAssignableBy(typeDeclaration);
    }

    @Override
    public void resolve(Types types, ResolvedReferenceTypeDeclaration typeDeclaration,
                        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        if (typeParametersMap != null && typeParametersMap.size() == 1) {
            ArrayNode arrayNode = ObjectMappers.instance().createArrayNode();
            if (!"?".equals(typeParametersMap.get(0).b.describe())) {
                Types componentType = TypeResolvers.of(typeParametersMap.get(0).b).duplicate();
                componentType.prefix("[].");
                if (componentType.isResolved()) {
                    arrayNode.addPOJO(componentType.getValue());
                    types.getCells().addAll(componentType.getCells());
                }
            }
            types.setValue(arrayNode);
        }
    }
}
