package com.apigcc.common.resolver;

import com.apigcc.common.description.ArrayTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.description.UnAvailableTypeDescription;
import com.apigcc.common.helper.TypeParameterHelper;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

import java.util.Optional;

public class CollectionTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return isCollection(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        Optional<ResolvedType> optional = TypeParameterHelper.getTypeParameter(type.asReferenceType(), 0);
        return optional
                .<TypeDescription>map(resolvedType -> new ArrayTypeDescription(TypeResolvers.resolve(resolvedType)))
                .orElseGet(UnAvailableTypeDescription::new);
    }

    private static boolean isCollection(ResolvedType type){
        if(!type.isReferenceType()){
            return false;
        }
        return isCollection(type.asReferenceType().getId());
    }

    private static boolean isCollection(String id){
        return ImmutableList.of("java.util.List",
                "java.util.Collection",
                "java.util.ArrayList",
                "java.util.LinkedList",
                "java.util.Set",
                "java.util.HashSet",
                "java.util.Iterator",
                "java.lang.Iterable"
        ).contains(id);

    }
}
