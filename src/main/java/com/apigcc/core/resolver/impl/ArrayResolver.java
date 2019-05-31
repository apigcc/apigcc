package com.apigcc.core.resolver.impl;

import com.apigcc.core.common.ObjectMappers;
import com.apigcc.core.resolver.TypeResolvers;
import com.apigcc.core.resolver.Types;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.javaparser.resolution.types.ResolvedType;

public class ArrayResolver extends Resolver {

    @Override
    public boolean accept(ResolvedType resolvedType) {
        return super.accept(resolvedType) && resolvedType.isArray();
    }

    @Override
    public void resolve(Types types, ResolvedType resolvedType) {
        ArrayNode arrayNode = ObjectMappers.instance().createArrayNode();
        Types componentType = TypeResolvers.of(resolvedType.asArrayType().getComponentType()).duplicate();
        if (componentType.isResolved()) {
            componentType.prefix("[].");
            arrayNode.addPOJO(componentType.getValue());
            types.setValue(arrayNode);
            types.getCells().addAll(componentType.getCells());
        }
    }
}
