package com.apigcc.core.resolver;

import com.apigcc.core.common.description.PrimitiveTypeDescription;
import com.apigcc.core.common.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

public class PrimitiveTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return type.isPrimitive() || isBoxing(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        if(type.isPrimitive()){
            return new PrimitiveTypeDescription(type.asPrimitive());
        }else{
            return new PrimitiveTypeDescription(type.asReferenceType());
        }
    }

    private static boolean isBoxing(ResolvedType type){
        if(!type.isReferenceType()){
            return false;
        }
        String id = type.asReferenceType().getId();
        return ImmutableList.of(
                "java.lang.Boolean",
                "java.lang.Character",
                "java.lang.Double",
                "java.lang.Float",
                "java.lang.Long",
                "java.lang.Integer",
                "java.lang.Short",
                "java.lang.Byte"
        ).contains(id);
    }

}
