package com.github.apiggs.ast;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.apiggs.Environment;
import com.github.apiggs.util.Cell;
import com.github.apiggs.util.ObjectMappers;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.utils.Pair;
import com.google.common.base.Strings;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 语法树中Symbol处理
 * 解析已知类的结构
 */
public class ResolvedTypes {

    /**
     * 已解析类型结果池，防止循环递归
     */
    private static Map<Object, ResolvedTypes> POOL = new ConcurrentHashMap<>();

    /**
     * 获取解析结果前，应判断是否已解析
     */
    public boolean resolved;
    /**
     * 是否基本类型
     */
    public boolean primitive;
    public String name;
    public Object value;
    public ObjectNode objectNode;
    public List<Cell<String>> cells = new ArrayList<>();

    public Object getValue() {
        return value == null ? objectNode : value;
    }

    /**
     * 解析普通类型
     *
     * @param type
     * @return
     */
    public static ResolvedTypes of(Type type) {
        try {
            ResolvedType resolvedType = type.resolve();
            return of(resolvedType);
        } catch (UnsolvedSymbolException e) {
            //解析失败 查找泛型参数
            return tryResolveTypeArguments(type);
        }
    }

    /**
     * 包装解析类型
     *
     * @param resolvedType
     * @return
     */
    public static ResolvedTypes of(ResolvedType resolvedType) {
        if (POOL.containsKey(resolvedType)) {
            return POOL.get(resolvedType).duplicate();
        }
        ResolvedTypes resolvedTypes = new ResolvedTypes();
        POOL.put(resolvedType, resolvedTypes);
        if (!resolvedType.isTypeVariable()) {
            resolvedTypes.resolve(resolvedType);
            resolvedTypes.resolved = true;
        }
        return resolvedTypes.duplicate();
    }

    /**
     * 只解析类型的定义，用于解析字符串类名时，只能获取到定义
     *
     * @param typeDeclaration
     * @return
     */
    public static ResolvedTypes of(ResolvedReferenceTypeDeclaration typeDeclaration) {
        if (POOL.containsKey(typeDeclaration)) {
            return POOL.get(typeDeclaration).duplicate();
        }
        ResolvedTypes resolvedTypes = new ResolvedTypes();
        POOL.put(typeDeclaration, resolvedTypes);
        resolvedTypes.name = typeDeclaration.getName();
        resolvedTypes.resolved = true;
        resolvedTypes.resolve(typeDeclaration, null);
        return resolvedTypes;
    }

    /**
     * 解析泛型的参数类型，需结合外部环境
     *
     * @param type
     * @param typeParametersMap
     * @return
     */
    public static ResolvedTypes ofTypeVariable(ResolvedType type, List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {

        if (!type.isTypeVariable()) {
            return ResolvedTypes.of(type);
        }
        //泛型解析
        ResolvedTypeVariable resolvedType = type.asTypeVariable();
        if (typeParametersMap != null) {
            for (int i = 0; i < typeParametersMap.size(); i++) {
                Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair = typeParametersMap.get(i);
                if (Objects.equals(resolvedType.asTypeParameter(), pair.a)) {
                    return ResolvedTypes.of(pair.b);
                }
            }
        }
        // 未成功解析
        return new ResolvedTypes();
    }

    /**
     * 解析泛型参数
     *
     * @param type
     */
    private static ResolvedTypes tryResolveTypeArguments(Type type) {
        ResolvedTypes typeArgumentResolvedTypes = new ResolvedTypes();
        if (type instanceof ClassOrInterfaceType) {
            ClassOrInterfaceType classType = (ClassOrInterfaceType) type;
            if (classType.getTypeArguments().isPresent()) {
                List<ResolvedTypes> types = new ArrayList<>();
                for (int i = 0; i < classType.getTypeArguments().get().size(); i++) {
                    Type typeArgument = classType.getTypeArguments().get().get(i);
                    ResolvedTypes argumentResolved = of(typeArgument);
                    if (argumentResolved.resolved) {
                        types.add(argumentResolved);
                    }
                }

                if (types.size() == 1) {
                    ResolvedTypes resolvedTypes = types.get(0);
                    typeArgumentResolvedTypes.value = resolvedTypes.getValue();
                    typeArgumentResolvedTypes.resolved = true;
                    typeArgumentResolvedTypes.cells.addAll(resolvedTypes.cells);
                } else {
                    ArrayNode arrayNode = ObjectMappers.instance().createArrayNode();
                    int i = 0;
                    for (ResolvedTypes resolvedTypes : types) {
                        String field = "?" + i++;
                        resolvedTypes.prefix(field + ".");
                        arrayNode.addPOJO(resolvedTypes.getValue());
                        typeArgumentResolvedTypes.resolved = true;
                        typeArgumentResolvedTypes.cells.addAll(resolvedTypes.cells);
                    }
                    typeArgumentResolvedTypes.value = arrayNode;
                }

            }
        }
        return typeArgumentResolvedTypes;
    }

    /**
     * 基本解析流程
     *
     * @param resolvedType type解析之后的类型
     */
    private void resolve(ResolvedType resolvedType) {

        resolveName(resolvedType);

        //忽略的类型
        if (Environment.getIgnoreTypes().contains(this.name)) {
            return;
        }

        if (resolvedType.isPrimitive() || Types.Numbers.isAssignableBy(resolvedType)) {
            primitive = true;
            value = Defaults.DEFAULT_INTEGER;
        } else if (Types.CharSequences.isAssignableBy(resolvedType)) {
            primitive = true;
            value = Defaults.DEFAULT_STRING;
        } else if (resolvedType.isArray()) {
            resolveArray(resolvedType);
        } else if (resolvedType.isReferenceType()) {
            ResolvedReferenceType referenceType = resolvedType.asReferenceType();
            ResolvedReferenceTypeDeclaration typeDeclaration = referenceType.getTypeDeclaration();
            List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap = referenceType.getTypeParametersMap();
            resolve(typeDeclaration, typeParametersMap);
        }

    }

    private void resolve(ResolvedReferenceTypeDeclaration typeDeclaration, List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        if (typeDeclaration.isEnum()) {
            //枚举类型
            primitive = true;
            value = "";
        } else if (Types.Collections.isAssignableBy(typeDeclaration)) {
            resolveCollection(typeParametersMap);
        } else if (Types.Maps.isAssignableBy(typeDeclaration)) {
            //Map类型，解析为一个object
            value = Defaults.DEFAULT_MAP;
        } else if (Types.Dates.isAssignableBy(typeDeclaration)) {
            //TODO 日期格式 从配置中 从注解中读取日期格式
            value = Defaults.DEFAULT_STRING;
        } else if (Types.Langs.isAssignableBy(typeDeclaration)) {
            //java包中的类型，不处理
        } else {
            resolvePojo(typeDeclaration, typeParametersMap);
        }
    }

    /**
     * 解析类型的名称
     * 基本类型如：int double
     * java类型如：String List
     * 自定义类型如: com.example.User
     *
     * @param resolvedType
     * @return
     */
    private void resolveName(ResolvedType resolvedType) {
        name = resolvedType.describe();
        if (resolvedType instanceof ReferenceTypeImpl) {
            name = ((ReferenceTypeImpl) resolvedType).getTypeDeclaration().getName();
        }
    }

    /**
     * 解析数组
     *
     * @param resolvedType
     */
    private void resolveArray(ResolvedType resolvedType) {
        if (resolvedType.isArray()) {
            ArrayNode arrayNode = ObjectMappers.instance().createArrayNode();
            ResolvedTypes componentType = ResolvedTypes.of(resolvedType.asArrayType().getComponentType());
            componentType.prefix("[].");
            if (componentType.resolved) {
                arrayNode.addPOJO(componentType.getValue());
                value = arrayNode;
                cells.addAll(componentType.cells);
            }
        }
    }

    /**
     * 解析集合
     *
     * @param typeParametersMap
     */
    private void resolveCollection(List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        if (typeParametersMap != null && typeParametersMap.size() == 1) {
            ArrayNode arrayNode = ObjectMappers.instance().createArrayNode();
            if (!"?".equals(typeParametersMap.get(0).b.describe())) {
                ResolvedTypes componentType = ResolvedTypes.of(typeParametersMap.get(0).b);
                componentType.prefix("[].");
                if (componentType.resolved) {
                    arrayNode.addPOJO(componentType.getValue());
                    cells.addAll(componentType.cells);
                }
            }
            value = arrayNode;
        }
    }

    /**
     * 解析Pojo类，String 类
     * 递归解析父类，直到java.lang.Object
     *
     * @param typeDeclaration
     * @param typeParametersMap
     */
    private void resolvePojo(ResolvedReferenceTypeDeclaration typeDeclaration, List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        objectNode = ObjectMappers.instance().createObjectNode();
        resolveFields(typeDeclaration, typeParametersMap);
    }

    /**
     * 解析类型各字段
     *
     * @param typeDeclaration
     */
    private void resolveFields(ResolvedReferenceTypeDeclaration typeDeclaration, List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        //先解析父类的字段
        try {
            typeDeclaration.getAncestors().forEach(direct -> merge(ResolvedTypes.of(direct)));
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



            ResolvedTypes resolvedTypes = ResolvedTypes.ofTypeVariable(next.getType(), typeParametersMap);
            resolvedTypes.prefix(name + ".");

            String condition = "";

            if (next instanceof JavaParserFieldDeclaration) {
                JavaParserFieldDeclaration field = (JavaParserFieldDeclaration) next;
                if (Comments.isIgnore(field.getWrappedNode())) {
                    continue;
                }
                condition = Validations.of(field.getWrappedNode().getAnnotations()).getResults();

                description = Comments.getCommentAsString(field);

                Object value = Fields.getInitializer(field);
                if (value != null) {
                    resolvedTypes.value = value;
                }
            }

            put(name, resolvedTypes, condition, description);

        }
    }

    /**
     * 合并另一个Type的属性到当前对象
     *
     * @param other
     */
    private void merge(ResolvedTypes other) {
        if (other.resolved && !other.primitive) {
            if (other.getValue() instanceof ObjectNode) {
                ObjectNode directValue = (ObjectNode) other.getValue();
                objectNode.setAll(directValue);
            }
            cells.addAll(other.cells);
        }
    }

    /**
     * 设置某字段
     *
     * @param key
     * @param other
     * @param description
     */
    private void put(String key, ResolvedTypes other, String condition, String description) {
        if (other.resolved) {
            if (Objects.nonNull(other.getValue())) {
                objectNode.putPOJO(key, other.getValue());
            }
            Cell<String> cell = new Cell<>(key, other.name);

            if(Objects.nonNull(condition)){
                cell.add(condition);
            }else{
                cell.add("");
            }

            if (other.primitive) {
                cell.add(String.valueOf(other.getValue()));
            } else {
                cell.add("");
            }
            if (description != null) {
                cell.add(description);
            } else{
                cell.add("");
            }
            cells.add(cell);
            cells.addAll(other.cells);
        }
    }

    public void prefix(String prefix) {
        for (Cell<String> cell : cells) {
            cell.set(0, prefix + cell.get(0));
        }
    }

    private ResolvedTypes duplicate() {
        ResolvedTypes resolvedTypes = new ResolvedTypes();
        resolvedTypes.name = this.name;
        resolvedTypes.resolved = this.resolved;
        resolvedTypes.primitive = this.primitive;
        resolvedTypes.value = this.value;
        resolvedTypes.objectNode = this.objectNode;
        for (Cell<String> cell : this.cells) {
            resolvedTypes.cells.add(cell.duplicate());
        }
        return resolvedTypes;
    }

}
