package com.apigcc.common;

import com.apigcc.common.description.*;
import com.apigcc.common.helper.CommentHelper;
import com.apigcc.common.helper.FieldHelper;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.utils.Pair;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResolvedTypes {

    /**
     * 已解析的类型池，
     * 解决循环依赖问题
     */
    private static Map<String, ObjectTypeDescription> resolvedPool = new HashMap<>();

    /**
     * 获取类型信息
     * @param type
     * @return
     */
    public static TypeDescription resolve(Type type){
        try{
            ResolvedType resolvedType = type.resolve();
            return pick(resolvedType);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new StringTypeDescription(type.toString(),"");
    }

    /**
     * 解析类型信息
     * @param type
     * @return
     */
    public static TypeDescription pick(ResolvedType type){
        if (type.isPrimitive()) {
            return new PrimitiveTypeDescription(type.asPrimitive());
        }
        if (type.isArray()) {
            return new ArrayTypeDescription(pick(type.asArrayType().getComponentType()));
        }
        if (type.isReferenceType()){
            ResolvedReferenceType referenceType = type.asReferenceType();
            String id = referenceType.getId();
            if(isBoxing(id)){
                return new PrimitiveTypeDescription(referenceType);
            }else if(isString(id)){
                return new StringTypeDescription("String","");
            }else if(isCollection(id)) {
                ResolvedType typeParameter = getTypeParameter(referenceType, 0);
                if (typeParameter != null) {
                    return new ArrayTypeDescription(pick(typeParameter));
                }
            }else if(isSystem(id)) {
                //system class do nothing
                return new StringTypeDescription(referenceType.getQualifiedName(),"");
            }else if(isSpringComponent(id)){
                ResolvedType typeParameter = getTypeParameter(referenceType, 0);
                if (typeParameter != null) {
                    return pick(typeParameter);
                }
            }else{
                return pick(referenceType);
            }
        }
        return new StringTypeDescription(type.describe(),"");
    }

    public static ObjectTypeDescription pick(ResolvedReferenceType referenceType){
        if(resolvedPool.containsKey(referenceType.describe())){
            return resolvedPool.get(referenceType.describe());
        }
        ObjectTypeDescription typeDescription = new ObjectTypeDescription();
        resolvedPool.put(referenceType.describe(),typeDescription);

        for (ResolvedReferenceType directAncestor : referenceType.getDirectAncestors()) {
            if(isSystem(directAncestor.getId())){
               continue;
            }
            ObjectTypeDescription ancestorDescription = pick(directAncestor);
            typeDescription.merge(ancestorDescription);
        }

        for (ResolvedFieldDeclaration declaredField : referenceType.getTypeDeclaration().getDeclaredFields()) {
            TypeDescription fieldDescription = pick(declaredField.getType());
            fieldDescription.setKey(declaredField.getName());
            fieldDescription.setRemark(CommentHelper.getComment(declaredField));
            typeDescription.add(fieldDescription);
        }

        //TODO access method

        return typeDescription;
    }

    private static boolean isBoxing(String id){
        return ImmutableList.of(
                "java.lang.Boolean",
                "java.lang.Character",
                "java.lang.Double",
                "java.lang.Float",
                "java.lang.Long",
                "java.lang.Integer",
                "java.lang.Short",
                "java.lang.Byte"
                ).contains(id);
    }

    private static boolean isString(String id){
        return ImmutableList.of("java.lang.String",
                "java.lang.CharSequence"
                ).contains(id);
    }

    private static boolean isCollection(String id){
        return ImmutableList.of("java.util.List",
                "java.util.Collection",
                "java.lang.Iterable"
                ).contains(id);
    }

    private static boolean isSpringComponent(String id){
        return id!=null && (id.startsWith("org.springframework"));
    }

    private static boolean isSystem(String id){
        return id!=null && (id.startsWith("java") ||id.startsWith("sun"));
    }

    /**
     * 获取泛型信息
     * @param referenceType
     * @param index 位置信息
     * @return
     */
    private static ResolvedType getTypeParameter(ResolvedReferenceType referenceType, int index){
        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParameters = referenceType.getTypeParametersMap();
        if(typeParameters.size()>index){
            Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair = typeParameters.get(index);
            return pair.b;
        }
        return null;
    }

    /**
     * 获取泛型信息
     * @param referenceType
     * @param a 如 T E 等
     * @return
     */
    private static ResolvedType getTypeParameter(ResolvedReferenceType referenceType, String a){
        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParameters = referenceType.getTypeParametersMap();
        for (Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair : typeParameters) {
            if(Objects.equals(pair.a.getName(),a)){
                return pair.b;
            }
        }
        return null;
    }

}
