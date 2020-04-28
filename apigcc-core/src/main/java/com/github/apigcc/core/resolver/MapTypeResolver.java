package com.github.apigcc.core.resolver;

import com.github.apigcc.core.description.TypeDescription;
import com.github.apigcc.core.description.UnAvailableTypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * 不支持直接使用Map，建议使用DTO
 */
public class MapTypeResolver extends ReferenceTypeResolver {

    private static final ImmutableList<String> list = ImmutableList.of(
            Map.class.getName(),
            HashMap.class.getName(),
            Hashtable.class.getName(),
            SortedMap.class.getName(),
            LinkedHashMap.class.getName(),
            TreeMap.class.getName()
    );

    @Override
    public boolean accept(ResolvedType type) {
        return super.accept(type) && list.contains(type.asReferenceType().getId());
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new UnAvailableTypeDescription();
    }

}
