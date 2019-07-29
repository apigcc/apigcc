package com.apigcc.common.resolver;

import com.apigcc.common.description.ObjectTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.helper.ReferenceContext;
import com.apigcc.common.helper.CommentHelper;
import com.apigcc.common.helper.JsonPropertyHelper;
import com.apigcc.common.helper.TypeParameterHelper;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Optional;

public class ObjectTypeResolver implements TypeResolver {

    @Override
    public boolean accept(ResolvedType type) {
        return type.isReferenceType();
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {

        ResolvedReferenceType referenceType = type.asReferenceType();
        ObjectTypeDescription typeDescription = new ObjectTypeDescription();

        typeDescription.setType(referenceType.getTypeDeclaration().getName());
        //类型解析缓冲池，防止循环引用
        if(!ReferenceContext.getInstance().push(referenceType.describe())){
            return typeDescription;
        }

        //解析父类属性，并合并至当前
        for (ResolvedReferenceType directAncestor : referenceType.getDirectAncestors()) {
            TypeDescription ancestorDescription = TypeResolvers.resolve(directAncestor);
            if(ancestorDescription.isAvailable() && ancestorDescription.isObject()){
                typeDescription.merge(ancestorDescription.asObject());
            }
        }


        //TODO fix use access method
        for (ResolvedFieldDeclaration declaredField : referenceType.getTypeDeclaration().getDeclaredFields()) {
            if(declaredField.isStatic()){
                continue;
            }
            ResolvedType fieldType = declaredField.getType();
            String key = declaredField.getName();
            //查找json别名
            Optional<String> jsonName = JsonPropertyHelper.getJsonName(declaredField);
            if(jsonName.isPresent()){
                key = jsonName.get();
            }
            if(fieldType.isReferenceType()){
                //将父类的T，传递给 属性的T
                fieldType = TypeParameterHelper.useClassTypeParameter(referenceType,fieldType.asReferenceType());
            }
            if(declaredField.getType().isTypeVariable()){
                //类型为T，这种泛型
                Optional<ResolvedType> optional = TypeParameterHelper.getTypeParameter(referenceType, declaredField.getType().asTypeParameter().getName());
                if(optional.isPresent()){
                    fieldType = optional.get();
                }
            }
            TypeDescription fieldDescription = TypeResolvers.resolve(fieldType);
            fieldDescription.setKey(key);
            fieldDescription.addRemark(CommentHelper.getComment(declaredField));
            typeDescription.add(fieldDescription);
        }

        ReferenceContext.getInstance().remove(referenceType.describe());
        return typeDescription;
    }

}