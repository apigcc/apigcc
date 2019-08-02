package com.apigcc.core.resolver;

import com.apigcc.core.common.description.StringTypeDescription;
import com.apigcc.core.common.description.TypeDescription;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

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
