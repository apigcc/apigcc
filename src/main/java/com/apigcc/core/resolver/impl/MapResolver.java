package com.apigcc.core.resolver.impl;

import com.apigcc.core.common.ObjectMappers;
import com.apigcc.core.resolver.TypeResolvers;
import com.apigcc.core.resolver.Types;
import com.apigcc.core.resolver.ast.Clazz;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.utils.Pair;

import java.util.List;

public class MapResolver extends ReferenceResolver {


    @Override
    public boolean accept(ResolvedReferenceTypeDeclaration typeDeclaration) {
        return Clazz.Maps.isAssignableBy(typeDeclaration);
    }

    @Override
    public void resolve(Types types, ResolvedReferenceTypeDeclaration typeDeclaration,
                        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        if (typeParametersMap != null && typeParametersMap.size() == 2) {
            ObjectNode objectNode = ObjectMappers.instance().createObjectNode();
            Object key = null;
            Object value = null;
            if (!"?".equals(typeParametersMap.get(0).b.describe())) {
                Types componentType = TypeResolvers.of(typeParametersMap.get(0).b);
                if (componentType.isResolved()) {
                    key = componentType.getValue();
                }
            }
            if (!"?".equals(typeParametersMap.get(1).b.describe())) {
                Types componentType = TypeResolvers.of(typeParametersMap.get(1).b);
                if (componentType.isResolved()) {
                    value = componentType.getValue();
                }
            }
            if (key != null && value != null) {
                objectNode.putPOJO(String.valueOf(key), value);
                types.setValue(objectNode);
            }
        }
    }
}
