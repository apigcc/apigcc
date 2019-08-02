package com.apigcc.core.resolver;

import com.apigcc.core.common.description.TypeDescription;
import com.apigcc.core.common.description.UnAvailableTypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

/**
 * 不支持直接使用Map，建议使用DTO
 */
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
