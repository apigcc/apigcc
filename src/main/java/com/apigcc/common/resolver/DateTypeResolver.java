package com.apigcc.common.resolver;

import com.apigcc.common.description.StringTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

public class DateTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return isDate(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        return new StringTypeDescription(type.asReferenceType().getTypeDeclaration().getName(),"");
    }

    private static boolean isDate(ResolvedType type){
        if(!type.isReferenceType()){
            return false;
        }
        return isDate(type.asReferenceType().getId());
    }

    private static boolean isDate(String id){
        return ImmutableList.of("java.util.Date",
                "java.time.LocalDateTime",
                "java.time.LocalDate",
                "java.time.LocalTime"
        ).contains(id);
    }
}
