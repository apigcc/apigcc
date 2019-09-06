package com.apigcc.core.resolver;

import com.apigcc.core.Apigcc;
import com.apigcc.core.common.description.ObjectTypeDescription;
import com.apigcc.core.common.description.TypeDescription;
import com.apigcc.core.common.helper.*;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Optional;

/**
 * java bean解析
 */
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
            TypeDescription ancestorDescription = Apigcc.getInstance().getTypeResolvers().resolve(directAncestor);
            if(ancestorDescription.isAvailable() && ancestorDescription.isObject()){
                typeDescription.merge(ancestorDescription.asObject());
            }
        }


        //TODO fix use access method
        for (ResolvedFieldDeclaration declaredField : referenceType.getTypeDeclaration().getDeclaredFields()) {
            if(declaredField.isStatic()){
                continue;
            }

            if(CommentHelper.isIgnore(declaredField)){
                continue;
            }

            ResolvedType fieldType = declaredField.getType();

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

            TypeDescription fieldDescription = Apigcc.getInstance().getTypeResolvers().resolve(fieldType);
            fieldDescription.setKey(declaredField.getName());
            //查找json别名
            JsonPropertyHelper.getJsonName(declaredField).ifPresent(fieldDescription::setKey);
            //解析注释
            fieldDescription.addRemark(CommentHelper.getComment(declaredField));
            //查找Validation注解
            for (String validation : ValidationHelper.getValidations(declaredField)) {
                fieldDescription.getCondition().append(validation).append(" ");
            }
            //查找字段初始化值
            FieldHelper.getInitializer(declaredField).ifPresent(expr-> fieldDescription.setDefaultValue(ExpressionHelper.getValue(expr)));

            typeDescription.add(fieldDescription);
        }

        ReferenceContext.getInstance().remove(referenceType.describe());
        return typeDescription;
    }

}