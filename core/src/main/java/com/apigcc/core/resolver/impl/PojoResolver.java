package com.apigcc.core.resolver.impl;

import com.apigcc.core.common.Cell;
import com.apigcc.core.common.ObjectMappers;
import com.apigcc.core.resolver.TypeResolvers;
import com.apigcc.core.resolver.Types;
import com.apigcc.core.resolver.ast.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.utils.Pair;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PojoResolver extends ReferenceResolver {

    @Override
    public boolean accept(ResolvedReferenceTypeDeclaration typeDeclaration) {
        return !Clazz.Langs.isAssignableBy(typeDeclaration);
    }

    @Override
    public void resolve(Types types, ResolvedReferenceTypeDeclaration typeDeclaration,
                        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {

        ObjectNode objectNode = ObjectMappers.instance().createObjectNode();

        //先解析父类的字段
        try {
            typeDeclaration.getAncestors().forEach(direct -> {
                Types ancestor = TypeResolvers.of(direct);
                if (ancestor.isResolved() && !ancestor.isPrimitive()) {
                    if (ancestor.getValue() instanceof ObjectNode) {
                        ObjectNode directValue = (ObjectNode) ancestor.getValue();
                        objectNode.setAll(directValue);
                    }
                    types.getCells().addAll(ancestor.getCells());
                }
            });
        } catch (Exception e) {
        }

        //解析各字段
        Iterator<ResolvedFieldDeclaration> iterator = typeDeclaration.getDeclaredFields().iterator();
        while (iterator.hasNext()) {
            ResolvedFieldDeclaration next = iterator.next();
            if (next.isStatic() || typeDeclaration.equals(next.getType())) {
                continue;
            }
            String description = null;
            String name = Fields.getName(next);
            //处理类字段的默认值

            Types resolvedTypes = of(next.getType(), typeParametersMap).duplicate();
            resolvedTypes.prefix(name + ".");

            String condition = "";

            if (next instanceof JavaParserFieldDeclaration) {
                JavaParserFieldDeclaration field = (JavaParserFieldDeclaration) next;
                if (Comments.isIgnore(field.getWrappedNode())) {
                    continue;
                }
                condition = Validations.of(field.getWrappedNode().getAnnotations()).getResults();

                Object value = Fields.getInitializer(field);
                if (value != null) {
                    resolvedTypes.setValue(value);
                }

                Optional<Comments> comments = Comments.of(field.getWrappedNode().getComment());
                if (comments.isPresent()) {
                    description = comments.get().getContent();
                    for (Tag tag : comments.get().getTags()) {
                        if (tag.getName().equals(Tags.value.name())) {
                            resolvedTypes.setValue(tag.getContent());
                        }
                    }
                }

            }

            Cell<String> cell = new Cell<>(name, noNull(resolvedTypes.getName()),
                    noNull(condition));
            if(resolvedTypes.isPrimitive()){
                cell.add(noNull(resolvedTypes.getValue()));
            }else{
                cell.add("");
            }
            cell.add(noNull(description));
            if (Objects.nonNull(resolvedTypes.getValue())) {
                objectNode.putPOJO(name, resolvedTypes.getValue());
            }
            types.getCells().add(cell);
            types.getCells().addAll(resolvedTypes.getCells());

        }

        types.setValue(objectNode);
    }

    /**
     * 解析泛型的参数类型，需结合外部环境
     *
     * @param type
     * @param typeParametersMap
     * @return
     */
    public static Types of(ResolvedType type, List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        if (type.isTypeVariable()) {
            //泛型解析
            ResolvedTypeVariable resolvedType = type.asTypeVariable();
            if (typeParametersMap != null) {
                for (int i = 0; i < typeParametersMap.size(); i++) {
                    Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair = typeParametersMap.get(i);
                    if (Objects.equals(resolvedType.asTypeParameter(), pair.a)) {
                        return TypeResolvers.of(pair.b);
                    }
                }
            }
        }
        return TypeResolvers.of(type);
    }

    private String noNull(Object value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }

}
