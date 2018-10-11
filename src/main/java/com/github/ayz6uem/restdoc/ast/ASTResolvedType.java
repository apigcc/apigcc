package com.github.ayz6uem.restdoc.ast;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ayz6uem.restdoc.schema.Cell;
import com.github.ayz6uem.restdoc.util.ObjectMappers;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 语法树中Symbol处理
 * 解析已知类的结构
 */
@Slf4j
@Getter
@Setter
public class ASTResolvedType {

    String name;
    Object value;
    boolean primitive;
    List<Cell> cells = new ArrayList<>();

    public static ASTResolvedType of(Type type){
        try{
            ResolvedType resolve = type.resolve();
            return of(resolve);
        }catch (UnsolvedSymbolException e){
            log.warn("try to resolve fail:" + type.toString());
        }
        return null;
    }

    public static ASTResolvedType of(ResolvedType resolvedType) {
        ASTResolvedType type = new ASTResolvedType();
        type.parse(resolvedType);
        return type;
    }

    private void parse(ResolvedType resolvedType){
        name = resolvedType.describe();
        if (resolvedType.isPrimitive()) {
            setPrimitive(true);
            setValue(0);
        } else if (resolvedType.isArray()) {
            parseArray(resolvedType.asArrayType());
        } else if (resolvedType.isReferenceType()) {
            ResolvedReferenceType resolvedReferenceType = resolvedType.asReferenceType();
            parsePojo(resolvedReferenceType);
        }
    }

    /**
     * 解析数组
     * @param resolvedArrayType
     */
    private void parseArray(ResolvedArrayType resolvedArrayType){
        ArrayNode arrayNode = ObjectMappers.instance().createArrayNode();
        ASTResolvedType componentType = ASTResolvedType.of(resolvedArrayType.getComponentType());
        arrayNode.addPOJO(componentType.getValue());
        setValue(arrayNode);
        getCells().addAll(componentType.getCells());
    }

    /**
     * 解析Pojo类，String 类
     * 递归解析父类，知道java.lang.Object
     * @param resolvedReferenceType
     */
    private void parsePojo(ResolvedReferenceType resolvedReferenceType){
        if (Object.class.getName().equals(resolvedReferenceType.getId())) {
            return;
        }
        if (String.class.getName().equals(resolvedReferenceType.getId())) {
            setPrimitive(true);
            setValue("");
            return;
        }
        //TODO COLLECTIONS
        //TODO CLASS WITH T

        ObjectNode objectNode = ObjectMappers.instance().createObjectNode();
        List<ResolvedReferenceType> directAncestors = resolvedReferenceType.getDirectAncestors();
        for (int i = 0; i < directAncestors.size(); i++) {
            ResolvedReferenceType directAncestor = directAncestors.get(i);
            parseFields(directAncestor, objectNode);
        }
        parseFields(resolvedReferenceType,objectNode);
        setValue(objectNode);
    }

    /**
     * 解析类型的属性，构造Json对象和字段描述
     * @param resolvedReferenceType
     * @param objectNode
     */
    private void parseFields(ResolvedReferenceType resolvedReferenceType, ObjectNode objectNode){
        if (Object.class.getName().equals(resolvedReferenceType.getId())) {
            return;
        }
        Set<ResolvedFieldDeclaration> declaredFields = resolvedReferenceType.getDeclaredFields();
        Iterator<ResolvedFieldDeclaration> iterator = declaredFields.iterator();
        while (iterator.hasNext()) {
            ResolvedFieldDeclaration next = iterator.next();
            String key = next.getName();
            //TODO next value
            //TODO 解析注解
            //TODO 解析泛型属性
            ASTResolvedType keyType = ASTResolvedType.of(next.getType());
            objectNode.putPOJO(key, keyType.getValue());
            Cell cell = new Cell(key, keyType.getName(), keyType.isPrimitive() ? "" : keyType.getValue());
            cells.add(cell);
            cells.addAll(keyType.getCells());
        }
    }

    /**
     * 获取类型权限定名
     * @param n
     * @return
     */
    public static String getFullName(ClassOrInterfaceDeclaration n){
        return getPackageName(n) + "." + getNameInScope(n);
    }

    /**
     * 获取类型的包名，包括内部类
     * @param n
     * @return
     */
    public static String getPackageName(ClassOrInterfaceDeclaration n){
        if(n.getParentNode().isPresent()){
            if(n.getParentNode().get() instanceof CompilationUnit){
                CompilationUnit cu = (CompilationUnit) n.getParentNode().get();
                if(cu.getPackageDeclaration().isPresent()){
                    PackageDeclaration packageDeclaration = cu.getPackageDeclaration().get();
                    return packageDeclaration.getNameAsString();
                }
            }
            if(n.getParentNode().get() instanceof ClassOrInterfaceDeclaration){
                return getPackageName((ClassOrInterfaceDeclaration)n.getParentNode().get());
            }
        }
        return "";
    }

    /**
     * 获取内部类的名称
     * eg:
     * Auth.Login
     * @param n
     * @return
     */
    public static String getNameInScope(ClassOrInterfaceDeclaration n){
        StringBuilder stringBuilder = new StringBuilder();
        appendNameInScope(n, stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendNameInScope(ClassOrInterfaceDeclaration n, StringBuilder stringBuilder){
        stringBuilder.insert(0,n.getNameAsString());
        if(n.getParentNode().isPresent() && n.getParentNode().get() instanceof ClassOrInterfaceDeclaration){
            ClassOrInterfaceDeclaration scope = (ClassOrInterfaceDeclaration) n.getParentNode().get();
            stringBuilder.insert(0,".");
            appendNameInScope(scope,stringBuilder);
        }
    }

}
