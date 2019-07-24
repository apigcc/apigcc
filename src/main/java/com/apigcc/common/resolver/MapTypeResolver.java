package com.apigcc.common.resolver;

import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.description.UnAvailableTypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

public class MapTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return isMap(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new UnAvailableTypeDescription();
    }

    private static boolean isMap(ResolvedType type){
        if(!type.isReferenceType()){
            return false;
        }
        return isMap(type.asReferenceType().getId());
    }

    private static boolean isMap(String id){
        return ImmutableList.of("java.util.Map",
                "java.util.HashMap",
                "java.util.Hashtable",
                "java.util.SortedMap",
                "java.util.LinkedHashMap",
                "java.lang.TreeMap"
        ).contains(id);
    }
}
