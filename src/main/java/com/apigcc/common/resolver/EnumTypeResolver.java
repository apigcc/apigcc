package com.apigcc.common.resolver;

import com.apigcc.common.description.StringTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

public class EnumTypeResolver implements TypeResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return type.isReferenceType() && type.asReferenceType().getTypeDeclaration().isEnum();
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        ResolvedEnumDeclaration enumDeclaration = type.asReferenceType().getTypeDeclaration().asEnum();
        TypeDescription description = new StringTypeDescription(enumDeclaration.getName(),"");
        for (ResolvedEnumConstantDeclaration enumConstant : enumDeclaration.getEnumConstants()) {
            description.addRemark(enumConstant.getName());
        }
        return description;
    }
}
