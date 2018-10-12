package com.github.ayz6uem.restdoc.ast;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ayz6uem.restdoc.schema.Cell;
import com.github.ayz6uem.restdoc.util.ObjectMappers;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.utils.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 语法树中Symbol处理
 * 解析已知类的结构
 */
@Slf4j
@Getter
@Setter
public class ResolvedTypes {

    /**
     * 获取解析结果前，应判断是否已解析
     */
    boolean resolved;

    /**
     * 是否基本类型
     */
    boolean primitive;
    String name;
    Object value;
    List<Cell> cells = new ArrayList<>();

    /**
     * 解析普通类型
     * @param type
     * @return
     */
    public static ResolvedTypes of(Type type) {
        ResolvedTypes resolvedTypes = new ResolvedTypes();
        try {
            resolvedTypes.resolve(type.resolve());
            resolvedTypes.resolved = true;
        } catch (UnsolvedSymbolException e) {
            log.warn("try to resolve fail:" + type.toString());
            //解析失败 查找泛型参数
            if(type instanceof ClassOrInterfaceType){
                ClassOrInterfaceType classType = (ClassOrInterfaceType) type;
                if(classType.getTypeArguments().isPresent()){
                    resolvedTypes.tryResolveTypeArguments(classType.getTypeArguments().get());
                    resolvedTypes.resolved = true;
                }
            }
        }
        return resolvedTypes;
    }

    /**
     * 包装解析类型
     * @param resolvedType
     * @return
     */
    public static ResolvedTypes of(ResolvedType resolvedType) {
        ResolvedTypes resolvedTypes = new ResolvedTypes();
        resolvedTypes.resolve(resolvedType);
        resolvedTypes.resolved = true;
        return resolvedTypes;
    }

    /**
     * 解析泛型的参数类型，需结合外部环境
     * @param resolvedType
     * @param typeParametersMap
     * @return
     */
    public static ResolvedTypes ofTypeVariable(ResolvedTypeVariable resolvedType, List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap) {
        for (int i = 0; i < typeParametersMap.size(); i++) {
            Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair = typeParametersMap.get(i);
            if(Objects.equals(resolvedType.asTypeParameter(), pair.a)){
                return ResolvedTypes.of(pair.b);
            }
        }
        return new ResolvedTypes();
    }

    private void tryResolveTypeArguments(NodeList<Type> nodes){
        nodes.forEach(type->{
            ResolvedTypes argumentResolved = ResolvedTypes.of(type);
            if(argumentResolved.isResolved()){
                setValue(argumentResolved.getValue());
                getCells().addAll(argumentResolved.getCells());
            }
        });
    }

    /**
     * 基本解析流程
     * @param resolvedType
     */
    private void resolve(ResolvedType resolvedType) {
        resolveName(resolvedType);
        resolvePrimitive(resolvedType);
        resolveArray(resolvedType);

        if (resolvedType.isReferenceType()) {
            ResolvedReferenceType referenceType = resolvedType.asReferenceType();
            if (referenceType.getId().equals(String.class.getName())) {
                setPrimitive(true);
                setValue("");
//            resolveCollection(referenceType);
            }else{
                resolvePojo(referenceType);
            }
        }
    }

    /**
     * 解析类型的名称
     * 基本类型如：int double
     * java类型如：String List
     * 自定义类型如: com.example.User
     * @param resolvedType
     * @return
     */
    private void resolveName(ResolvedType resolvedType){
        name = resolvedType.describe();
        if(resolvedType instanceof ReferenceTypeImpl){
            name = ((ReferenceTypeImpl) resolvedType).getTypeDeclaration().getName();
        }
    }

    /**
     * 解析基本类型
     * @param resolvedType
     */
    private void resolvePrimitive(ResolvedType resolvedType){
        if (resolvedType.isPrimitive()) {
            setPrimitive(true);
            setValue(0);
        }
    }

    /**
     * 解析数组
     * @param resolvedType
     */
    private void resolveArray(ResolvedType resolvedType){
        if (resolvedType.isArray()) {
            ArrayNode arrayNode = ObjectMappers.instance().createArrayNode();
            ResolvedTypes componentType = ResolvedTypes.of(resolvedType.asArrayType().getComponentType());
            if(componentType.isResolved()){
                arrayNode.addPOJO(componentType.getValue());
                setValue(arrayNode);
                getCells().addAll(componentType.getCells());
            }
        }
    }

    /**
     * 解析集合
     * @param referenceType
     */
    private void resolveCollection(ResolvedReferenceType referenceType){

    }

    /**
     * 解析Pojo类，String 类
     * 递归解析父类，直到java.lang.Object
     *
     * @param resolvedReferenceType
     */
    private void resolvePojo(ResolvedReferenceType resolvedReferenceType) {
        if (Object.class.getName().equals(resolvedReferenceType.getId())) {
            return;
        }
        ObjectNode objectNode = ObjectMappers.instance().createObjectNode();
        resolveFields(resolvedReferenceType, objectNode);
        setValue(objectNode);
    }

    /**
     * 解析类型各字段
     * @param referenceType
     * @param objectNode
     */
    private void resolveFields(ResolvedReferenceType referenceType, ObjectNode objectNode) {
        if (Object.class.getName().equals(referenceType.getId())) {
            return;
        }
        //先解析父类的字段
        List<ResolvedReferenceType> directAncestors = referenceType.getDirectAncestors();
        for (int i = 0; i < directAncestors.size(); i++) {
            ResolvedReferenceType directAncestor = directAncestors.get(i);
            resolveFields(directAncestor, objectNode);
        }

        //解析各字段
        Iterator<ResolvedFieldDeclaration> iterator = referenceType.getTypeDeclaration().getDeclaredFields().iterator();
        while (iterator.hasNext()) {
            ResolvedFieldDeclaration next = iterator.next();
            String name = next.getName();
            ResolvedTypes resolvedTypes;

            ResolvedType type = next.getType();
            if(type.isTypeVariable()){
                //泛型解析
                ResolvedTypeVariable resolvedTypeVariable = type.asTypeVariable();
                List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParametersMap = referenceType.getTypeParametersMap();
                resolvedTypes = ResolvedTypes.ofTypeVariable(resolvedTypeVariable, typeParametersMap);
            }else{
                resolvedTypes = ResolvedTypes.of(type);
            }

            if(resolvedTypes.isResolved()){
                if (Objects.nonNull(resolvedTypes.getValue())) {
                    objectNode.putPOJO(name, resolvedTypes.getValue());
                }
                Cell cell = new Cell(name, resolvedTypes.getName());
                if (resolvedTypes.isPrimitive()) {
                    cell.setValue(String.valueOf(resolvedTypes.getValue()));
                    //处理类字段的默认值
                    Optional<Expression> initializer = ((JavaParserFieldDeclaration) next).getVariableDeclarator().getInitializer();
                    initializer.ifPresent(expression -> cell.setValue(expression.toString()));
                }
                cell.setDescription(Comments.getCommentAsString(next));
                cells.add(cell);
                cells.addAll(resolvedTypes.getCells());
            }
        }
    }

    /**
     * 获取类型权限定名
     *
     * @param n
     * @return
     */
    public static String getFullName(ClassOrInterfaceDeclaration n) {
        return getPackageName(n) + "." + getNameInScope(n);
    }

    /**
     * 获取类型的包名，包括内部类
     *
     * @param n
     * @return
     */
    public static String getPackageName(ClassOrInterfaceDeclaration n) {
        if (n.getParentNode().isPresent()) {
            if (n.getParentNode().get() instanceof CompilationUnit) {
                CompilationUnit cu = (CompilationUnit) n.getParentNode().get();
                if (cu.getPackageDeclaration().isPresent()) {
                    PackageDeclaration packageDeclaration = cu.getPackageDeclaration().get();
                    return packageDeclaration.getNameAsString();
                }
            }
            if (n.getParentNode().get() instanceof ClassOrInterfaceDeclaration) {
                return getPackageName((ClassOrInterfaceDeclaration) n.getParentNode().get());
            }
        }
        return "";
    }

    /**
     * 获取内部类的名称
     * eg:
     * Auth.Login
     *
     * @param n
     * @return
     */
    public static String getNameInScope(ClassOrInterfaceDeclaration n) {
        StringBuilder stringBuilder = new StringBuilder();
        appendNameInScope(n, stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendNameInScope(ClassOrInterfaceDeclaration n, StringBuilder stringBuilder) {
        stringBuilder.insert(0, n.getNameAsString());
        if (n.getParentNode().isPresent() && n.getParentNode().get() instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration scope = (ClassOrInterfaceDeclaration) n.getParentNode().get();
            stringBuilder.insert(0, ".");
            appendNameInScope(scope, stringBuilder);
        }
    }

}
