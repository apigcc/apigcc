package com.github.apigcc.core.resolver;

import com.github.apigcc.core.description.StringTypeDescription;
import com.github.apigcc.core.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

public class StringTypeResolver extends ReferenceTypeResolver {

    private static final ImmutableList list = ImmutableList.of(
            String.class.getName(),
            CharSequence.class.getName()
    );

    @Override
    public boolean accept(ResolvedType type) {
        return super.accept(type) && list.contains(type.asReferenceType().getId());
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new StringTypeDescription("String", "");
    }

}
