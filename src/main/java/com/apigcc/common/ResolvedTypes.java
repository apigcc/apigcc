package com.apigcc.common;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.utils.Pair;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.reflect.Reflection;
import sun.reflect.FieldAccessor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class ResolvedTypes {

    public static TypeDescription resolve(Type type){
        try{
            ResolvedType resolvedType = type.resolve();
            return pick(resolvedType);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new TypeDescription();
    }

    public static TypeDescription pick(ResolvedType type){
        TypeDescription result = new TypeDescription();
        if (type.isPrimitive()) {
            result.number(0);
            return result;
        }
        if (type.isArray()) {
            ResolvedType componentType = type.asArrayType().getComponentType();
            result.array(pick(componentType));
            return result;
        }
        if (type.isReferenceType()){
            String id = type.asReferenceType().getId();
            if(isBoxing(id)){
                result.number(0);
            }else if(isString(id)){
                result.charSequence("");
            }else if(isCollection(id)){
                ResolvedType typeParameter = getTypeParameter(type.asReferenceType(), 0);
                if(typeParameter!=null){
                    result.array(pick(typeParameter));
                }
            }else{
                result = ResolvedTypes.pick(type.asReferenceType());
            }
        }
        return result;
    }

    public static TypeDescription pick(ResolvedReferenceType resolvedReferenceType){
        TypeDescription typeDescription = new TypeDescription();
        ResolvedReferenceTypeDeclaration typeDeclaration = resolvedReferenceType.getTypeDeclaration();
        if(typeDeclaration instanceof ReflectionClassDeclaration){
            ReflectionClassDeclaration classDeclaration = (ReflectionClassDeclaration) typeDeclaration;
//            classDeclaration.getAllFields().forEach(it->{
//                if(!it.isStatic() && it.accessSpecifier() != AccessSpecifier.PRIVATE){
//                    typeDescription.object(it.getName(), pick(it.getType()));
//                }
//            });
            classDeclaration.getAllMethods().forEach(it -> {
                if(it.getName().startsWith("is") || it.getName().startsWith("get")){
                    String fieldName = getFieldName(it.getName());
                    typeDescription.object(fieldName, pick(it.returnType()));

                }
            });
        }
        return typeDescription;
    }

    private static boolean isBoxing(String id){
        return ImmutableList.of("java.lang.Integer",
                "java.lang.Short",
                "java.lang.Long",
                "java.lang.Float",
                "java.lang.Double",
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

    private static ResolvedType getTypeParameter(ResolvedReferenceType referenceType, int index){
        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParameters = referenceType.getTypeParametersMap();
        if(typeParameters.size()>index){
            Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair = typeParameters.get(index);
            return pair.b;
        }
        return null;
    }

    private static ResolvedType getTypeParameter(ResolvedReferenceType referenceType, String a){
        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParameters = referenceType.getTypeParametersMap();
        for (Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair : typeParameters) {
            if(Objects.equals(pair.a.getName(),a)){
                return pair.b;
            }
        }
        return null;
    }

    private static String getFieldName(String accessMethodName){
        if(accessMethodName.startsWith("is")){
            String first = accessMethodName.substring(2, 3);
            String less = accessMethodName.substring(3);
            return first.toLowerCase() + less;
        }
        if(accessMethodName.startsWith("get")){
            String first = accessMethodName.substring(3, 4);
            String less = accessMethodName.substring(4);
            return first.toLowerCase() + less;
        }
        return null;
    }

}
