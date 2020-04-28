package com.github.apigcc.core.resolver;

import com.github.apigcc.core.description.StringTypeDescription;
import com.github.apigcc.core.description.TypeDescription;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.stream.Collectors;

public class EnumTypeResolver extends ReferenceTypeResolver {

    @Override
    public boolean accept(ResolvedType type) {
        return super.accept(type) && type.asReferenceType().getTypeDeclaration().isEnum();
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        ResolvedEnumDeclaration enumDeclaration = type.asReferenceType().getTypeDeclaration().asEnum();
        TypeDescription description = new StringTypeDescription(enumDeclaration.getName(), "");
        String desc = enumDeclaration.getEnumConstants().stream().map(ResolvedEnumConstantDeclaration::getName).collect(Collectors.joining(",", "[", "]"));
        description.addRemark(desc);
        return description;
    }
}
