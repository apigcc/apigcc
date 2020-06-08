package com.github.apigcc.core.resolver;

import com.github.apigcc.core.description.PrimitiveTypeDescription;
import com.github.apigcc.core.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

/**
 * 基础类型解析
 */
public class PrimitiveTypeResolver implements TypeResolver {

    private final static ImmutableList<String> list = ImmutableList.of(
            Boolean.class.getName(),
            Character.class.getName(),
            Double.class.getName(),
            Float.class.getName(),
            Long.class.getName(),
            Integer.class.getName(),
            Short.class.getName(),
            Byte.class.getName()
    );

    @Override
    public boolean accept(ResolvedType type) {
        return type.isPrimitive() || isBoxing(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        if (type.isPrimitive()) {
            return new PrimitiveTypeDescription(type.asPrimitive());
        } else {
            return new PrimitiveTypeDescription(type.asReferenceType());
        }
    }

    /**
     * 包装类型判断
     *
     * @param type
     * @return
     */
    private boolean isBoxing(ResolvedType type) {
        if (!type.isReferenceType()) {
            return false;
        }
        String id = type.asReferenceType().getId();
        return list.contains(id);
    }

}
