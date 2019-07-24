package com.apigcc.common.resolver;

import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.description.UnAvailableTypeDescription;
import com.apigcc.common.helper.TypeParameterHelper;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Optional;

public class SpringComponentTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return isSpringComponent(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        Optional<ResolvedType> optional = TypeParameterHelper.getTypeParameter(type.asReferenceType(), 0);
        return optional
                .map(TypeResolvers::resolve)
                .orElseGet(UnAvailableTypeDescription::new);
    }

    private static boolean isSpringComponent(ResolvedType type){
        if(!type.isReferenceType()){
            return false;
        }
        return isSpringComponent(type.asReferenceType().getId());
    }

    private static boolean isSpringComponent(String id){
        return id!=null && (id.startsWith("org.springframework"));
    }
}
