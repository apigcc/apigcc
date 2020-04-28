package com.github.apigcc.core.resolver;

import com.github.apigcc.core.description.StringTypeDescription;
import com.github.apigcc.core.description.TypeDescription;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class DateTypeResolver extends ReferenceTypeResolver {

    private static final ImmutableList<String> list = ImmutableList.of(
            Date.class.getName(),
            LocalDateTime.class.getName(),
            LocalDate.class.getName(),
            LocalTime.class.getName()
    );

    @Override
    public boolean accept(ResolvedType type) {
        return super.accept(type) && list.contains(type.asReferenceType().getId());
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        //TODO Date 默认值
        return new StringTypeDescription(type.asReferenceType().getTypeDeclaration().getName(), "");
    }
}
