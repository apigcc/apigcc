package com.apigcc.core.resolver;

import com.apigcc.core.Apigcc;
import com.apigcc.core.common.description.ArrayTypeDescription;
import com.apigcc.core.common.description.TypeDescription;
import com.apigcc.core.common.description.UnAvailableTypeDescription;
import com.apigcc.core.common.helper.TypeParameterHelper;
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
        return new ArrayTypeDescription(optional
                .map(Apigcc.getInstance().getTypeResolvers()::resolve)
                .orElseGet(UnAvailableTypeDescription::new));
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
