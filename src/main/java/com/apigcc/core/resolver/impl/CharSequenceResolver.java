package com.apigcc.core.resolver.impl;

import com.apigcc.core.resolver.Types;
import com.apigcc.core.resolver.ast.Clazz;
import com.apigcc.core.resolver.ast.Defaults;
import com.github.javaparser.resolution.types.ResolvedType;

public class CharSequenceResolver extends Resolver {

    @Override
    public boolean accept(ResolvedType resolvedType) {
        return super.accept(resolvedType) && Clazz.CharSequences.isAssignableBy(resolvedType);
    }

    @Override
    public void resolve(Types types, ResolvedType resolvedType) {
        types.setPrimitive(true);
        types.setValue(Defaults.DEFAULT_STRING);
    }
}
