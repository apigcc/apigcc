package com.apigcc.core.resolver;

import com.apigcc.core.Context;
import com.apigcc.common.ObjectMappers;
import com.apigcc.core.resolver.ast.Clazz;
import com.apigcc.core.resolver.impl.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class TypeResolvers {

    private static List<Resolver> resolvers = Lists.newArrayList(
            new ArrayResolver(), new CharSequenceResolver(), new PrimitiveResolver());

    private static List<ReferenceResolver> referenceResolvers = Lists.newArrayList(
            new CollectionResolver(), new DateResolver(), new EnumResolver(), new MapResolver(), new PojoResolver());

    static {
        resolvers.addAll(referenceResolvers);
    }

    /**
     * 解析类型
     *
     * @param type
     * @return
     */
    public static Types of(Type type) {
        if(!Types.contain(type)){
            try {
                ResolvedType resolvedType = type.resolve();
                Types.put(type, of(resolvedType));
            } catch (UnsolvedSymbolException e) {
                //解析失败 查找泛型参数
                Types.put(type, tryResolveTypeArguments(type));
            }
        }
        return Types.get(type);
    }

    public static Types of(ResolvedType resolvedType) {
        if(!Types.contain(resolvedType)){
            Types types = Types.get(resolvedType);
            Types.put(resolvedType, types);
            types.setName(Clazz.getName(resolvedType));
            for (Resolver resolver : resolvers) {
                if(resolver.accept(resolvedType)){
                    resolver.resolve(types, resolvedType);
                    types.setResolved(true);
                    break;
                }
            }
        }
        return Types.get(resolvedType);
    }

    /**
     * 解析泛型参数
     * 只有一个泛型时，直接返回解析这一个泛型
     * 如果有多个泛型，解析为{?0:T,?1:E}
     *
     * @param type
     */
    private static Types tryResolveTypeArguments(Type type) {
        if (type instanceof ClassOrInterfaceType) {
            ClassOrInterfaceType classType = (ClassOrInterfaceType) type;
            if (classType.getTypeArguments().isPresent()) {
                NodeList<Type> typeNodeList = classType.getTypeArguments().get();
                List<Types> typesList = new ArrayList<>();
                for (Type typeArgument : typeNodeList) {
                    Types types = of(typeArgument);
                    if(types.isResolved()){
                        if(typeNodeList.size() == 1){
                            return types;
                        }
                        typesList.add(types);
                    }
                }

                Types result = Types.get(type);
                ArrayNode arrayNode = ObjectMappers.instance.createArrayNode();
                int i = 0;
                for (Types types : typesList) {
                    String field = "?" + i++;
                    Types duplicate = types.duplicate();
                    duplicate.prefix(field + ".");
                    arrayNode.addPOJO(duplicate.getValue());
                    result.resolved = true;
                    result.cells.addAll(duplicate.cells);
                }
                result.value = arrayNode;
                return result;

            }
        }
        return Types.get(type);
    }

    public static Types tryParse(String name){
        SymbolReference<ResolvedReferenceTypeDeclaration> symbolReference = Context.getContext().getTypeSolver().tryToSolveType(name);
        if (symbolReference.isSolved()) {
            ResolvedReferenceTypeDeclaration typeDeclaration = symbolReference.getCorrespondingDeclaration();

            for (ReferenceResolver referenceResolver : referenceResolvers) {
                if(referenceResolver.accept(typeDeclaration)){
                    Types types = Types.get(typeDeclaration);
                    referenceResolver.resolve(types, typeDeclaration, null);
                    types.setResolved(true);
                    Types.put(name, types);
                    break;
                }
            }
        }
        return Types.get(name);
    }

}
