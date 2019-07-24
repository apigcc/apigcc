package com.apigcc.common.resolver;

import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.description.UnAvailableTypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;

public class SystemObjectTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return isSystem(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new UnAvailableTypeDescription();
    }

    private static boolean isSystem(ResolvedType type){
        if(!type.isReferenceType()){
            return false;
        }
        return isSystem(type.asReferenceType().getId());
    }

    private static boolean isSystem(String id){
        return id!=null && (id.startsWith("java") ||id.startsWith("sun"));
    }
}
