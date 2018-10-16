package com.github.ayz6uem.restdoc.ast;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ayz6uem.restdoc.Enviroment;
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
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 语法树中Symbol处理
 * 解析已知类的结构
 */
public class ResolvedTypes {

    static Logger log = LoggerFactory.getLogger(ResolvedTypes.class);

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
    public List<Cell> cells = new ArrayList<>();


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
        ResolvedTypes resolvedTypes = new ResolvedTypes();
        try {
            resolvedTypes.resolve(type.resolve());
            resolvedTypes.resolved = true;
        } catch (UnsolvedSymbolException e) {
            log.debug("try to resolve fail:" + type.toString());
            //解析失败 查找泛型参数
            if (type instanceof ClassOrInterfaceType) {
                ClassOrInterfaceType classType = (ClassOrInterfaceType) type;
                if (classType.getTypeArguments().isPresent()) {
                    resolvedTypes.tryResolveTypeArguments(classType.getTypeArguments().get());
                    resolvedTypes.resolved = true;
                }
            }
        }
        return resolvedTypes;
    }

    /**
     * 包装解析类型
     *
     * @param resolvedType
     * @return
     */
    public static ResolvedTypes of(ResolvedType resolvedType) {
        ResolvedTypes resolvedTypes = new ResolvedTypes();
        if (!resolvedType.isTypeVariable()) {
            resolvedTypes.resolve(resolvedType);
            resolvedTypes.resolved = true;
        }
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

        for (int i = 0; i < typeParametersMap.size(); i++) {
            Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair = typeParametersMap.get(i);
            if (Objects.equals(resolvedType.asTypeParameter(), pair.a)) {
                return ResolvedTypes.of(pair.b);
            }
        }
        // 未成功解析
        return new ResolvedTypes();
    }

    /**
     * 解析泛型参数
     *
     * @param nodes
     */
    private void tryResolveTypeArguments(NodeList<Type> nodes) {
        nodes.forEach(type -> {
            ResolvedTypes argumentResolved = ResolvedTypes.of(type);
            if (argumentResolved.resolved) {
                value = argumentResolved.getValue();
                cells.addAll(argumentResolved.cells);
            }
        });
    }

    /**
     * 基本解析流程
     *
     * @param resolvedType
     */
    private void resolve(ResolvedType resolvedType) {

        resolveName(resolvedType);

        //忽略的类型
        if (Enviroment.ignoreTypes.contains(this.name)) {
            return;
        }

        if (resolvedType.isPrimitive() || Numbers.isAssignableBy(resolvedType)) {
            primitive = true;
            value = 0;
        } else if (Strings.isAssignableBy(resolvedType)) {
            primitive = true;
            value = "";
        } else if (resolvedType.isArray()) {
            resolveArray(resolvedType);
        } else if (resolvedType.isReferenceType()) {
            ResolvedReferenceType referenceType = resolvedType.asReferenceType();
            if (referenceType.getTypeDeclaration().isEnum()) {
                //枚举类型
                primitive = true;
                value = "";
            } else if (Collections.isAssignableBy(resolvedType)) {
                resolveCollection(referenceType);
            } else if (Maps.isAssignableBy(resolvedType)) {
                //Map类型，解析为一个object
            } else if (Dates.isAssignableBy(resolvedType)) {
                //日期类型
            } else if (Langs.isAssignableBy(resolvedType)) {
                //java类型，不处理
            } else {
                resolvePojo(referenceType);
            }
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
     * @param referenceType
     */
    private void resolveCollection(ResolvedReferenceType referenceType) {

    }

    /**
     * 解析Pojo类，String 类
     * 递归解析父类，直到java.lang.Object
     *
     * @param resolvedReferenceType
     */
    private void resolvePojo(ResolvedReferenceType resolvedReferenceType) {
        objectNode = ObjectMappers.instance().createObjectNode();
        resolveFields(resolvedReferenceType);
    }

    /**
     * 解析类型各字段
     *
     * @param referenceType
     */
    private void resolveFields(ResolvedReferenceType referenceType) {
        //先解析父类的字段
        try {
            referenceType.getDirectAncestors().forEach(direct -> merge(ResolvedTypes.of(direct)));
        } catch (Exception e) {
            log.warn("parse parent fail:" + referenceType);
        }

        //解析各字段
        Iterator<ResolvedFieldDeclaration> iterator = referenceType.getTypeDeclaration().getDeclaredFields().iterator();
        while (iterator.hasNext()) {
            ResolvedFieldDeclaration next = iterator.next();
            ResolvedTypes resolvedTypes = ResolvedTypes.ofTypeVariable(next.getType(), referenceType.getTypeParametersMap());
            //处理类字段的默认值
            if (next instanceof JavaParserFieldDeclaration) {
                JavaParserFieldDeclaration field = (JavaParserFieldDeclaration) next;
                if (field.isStatic()) {
                    //忽略静态属性
                    continue;
                }

                Optional<Expression> initializer = field.getVariableDeclarator().getInitializer();
                initializer.ifPresent(expr -> resolvedTypes.value = expr.toString());
            }

            String comment = Comments.getCommentAsString(next);

            put(next.getName(), resolvedTypes, comment);
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
    private void put(String key, ResolvedTypes other, String description) {
        if (other.resolved) {
            if (Objects.nonNull(other.getValue())) {
                objectNode.putPOJO(key, other.getValue());
            }
            Cell cell = new Cell(key, other.name);
            if (other.primitive) {
                cell.setValue(String.valueOf(other.getValue()));
            }
            cell.setDescription(description);
            cells.add(cell);
            cells.addAll(other.cells);
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
