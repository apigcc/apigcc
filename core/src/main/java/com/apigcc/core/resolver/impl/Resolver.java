package com.apigcc.core.resolver.impl;

import com.apigcc.core.Context;
import com.apigcc.core.resolver.Types;
import com.apigcc.core.resolver.ast.Clazz;
import com.github.javaparser.resolution.types.ResolvedType;

public abstract class Resolver {

    public boolean accept(ResolvedType resolvedType){
        return !Context.getContext().getIgnoreTypes().contains(Clazz.getName(resolvedType));
    }

    public abstract void resolve(Types types, ResolvedType resolvedType);

}
