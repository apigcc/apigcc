package com.apigcc.common.resolver;

import com.apigcc.common.description.StringTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

public class StringTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return isString(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new StringTypeDescription("String","");
    }

    private static boolean isString(ResolvedType type){
        if(!type.isReferenceType()){
            return false;
        }
        return isString(type.asReferenceType().getId());
    }

    private static boolean isString(String id){
        return ImmutableList.of("java.lang.String",
                "java.lang.CharSequence"
        ).contains(id);
    }
}
