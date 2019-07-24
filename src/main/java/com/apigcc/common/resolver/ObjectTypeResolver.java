package com.apigcc.common.resolver;

import com.apigcc.common.description.ObjectTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.helper.CommentHelper;
import com.apigcc.common.helper.TypeParameterHelper;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ObjectTypeResolver implements TypeResolver {

    /**
     * 已解析的类型池，
     * 解决循环依赖问题
     */
    private static Map<String, ObjectTypeDescription> resolvedPool = new HashMap<>();

    @Override
    public boolean accept(ResolvedType type) {
        return type.isReferenceType();
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        ResolvedReferenceType referenceType = type.asReferenceType();
        if(resolvedPool.containsKey(referenceType.describe())){
            return resolvedPool.get(referenceType.describe());
        }
        ObjectTypeDescription typeDescription = new ObjectTypeDescription();
        resolvedPool.put(referenceType.describe(),typeDescription);

        typeDescription.setType(referenceType.getTypeDeclaration().getName());

        for (ResolvedReferenceType directAncestor : referenceType.getDirectAncestors()) {
            TypeDescription ancestorDescription = TypeResolvers.resolve(directAncestor);
            if(ancestorDescription.isAvailable() && ancestorDescription.isObject()){
                typeDescription.merge(ancestorDescription.asObject());
            }
        }

        for (ResolvedFieldDeclaration declaredField : referenceType.getTypeDeclaration().getDeclaredFields()) {
            if(declaredField.isStatic()){
                continue;
            }
            ResolvedType fieldType = declaredField.getType();
            if(declaredField.getType().isTypeVariable()){
                Optional<ResolvedType> optional = TypeParameterHelper.getTypeParameter(referenceType, declaredField.getType().asTypeParameter().getName());
                if(optional.isPresent()){
                    fieldType = optional.get();
                }
            }
            if(fieldType!=null){
                TypeDescription fieldDescription = TypeResolvers.resolve(fieldType);
                fieldDescription.setKey(declaredField.getName());
                fieldDescription.setRemark(CommentHelper.getComment(declaredField));
                typeDescription.add(fieldDescription);
            }
        }

        //TODO access method

        return typeDescription;
    }

    public void clear(){
        resolvedPool.clear();
    }

}