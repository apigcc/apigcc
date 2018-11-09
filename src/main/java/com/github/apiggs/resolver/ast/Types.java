package com.github.apiggs.resolver.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Types {

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

    /**
     * Number类型工具类
     */
    public static class Collections {

        public static final Set<String> IDS = Sets.newHashSet(
                List.class.getName(),
                ArrayList.class.getName(),
                LinkedList.class.getName(),
                Set.class.getName(),
                HashSet.class.getName(),
                TreeSet.class.getName(),
                Collection.class.getName(),
                Iterable.class.getName()
        );

        /**
         * 判断是否是基本数字类型
         * @param typeDeclaration
         * @return
         */
        public static boolean isAssignableBy(ResolvedReferenceTypeDeclaration typeDeclaration){
            return IDS.contains(typeDeclaration.getId());
        }

    }

    /**
     * String类型工具类
     */
    public static class CharSequences {

        public static final Set<String> IDS = Sets.newHashSet(
                String.class.getName(),
                Character.class.getName(),
                CharSequence.class.getName()
        );

        /**
         * 判断是否是基本字符类型
         * @param resolvedType
         * @return
         */
        public static boolean isAssignableBy(ResolvedType resolvedType){
            if(resolvedType instanceof ReferenceTypeImpl){
                return IDS.contains(((ReferenceTypeImpl) resolvedType).getId());
            }
            return false;
        }

    }

    /**
     * Number类型工具类
     */
    public static class Dates {

        public static final Set<String> IDS = Sets.newHashSet(
                LocalDateTime.class.getName(),
                Date.class.getName()
        );

        /**
         * 判断是否是基本数字类型
         * @param typeDeclaration
         * @return
         */
        public static boolean isAssignableBy(ResolvedReferenceTypeDeclaration typeDeclaration){
            return IDS.contains(typeDeclaration.getId());
        }

    }

    /**
     * Number类型工具类
     */
    public static class Langs {

        /**
         * 判断是否是基本数字类型
         * @param typeDeclaration
         * @return
         */
        public static boolean isAssignableBy(ResolvedReferenceTypeDeclaration typeDeclaration){
            String id = typeDeclaration.getId();
            return id.startsWith("java");
        }

    }

    /**
     * Number类型工具类
     */
    public static class Maps {

        public static final Set<String> IDS = Sets.newHashSet(
                HashMap.class.getName(),
                LinkedHashMap.class.getName(),
                TreeMap.class.getName(),
                Map.class.getName()
        );

        /**
         * 判断是否是基本数字类型
         * @param typeDeclaration
         * @return
         */
        public static boolean isAssignableBy(ResolvedReferenceTypeDeclaration typeDeclaration){
            return IDS.contains(typeDeclaration.getId());
        }

    }

    /**
     * Number类型工具类
     */
    public static class Numbers {

        public static final Set<String> IDS = Sets.newHashSet(
                Byte.class.getName(),
                Short.class.getName(),
                Integer.class.getName(),
                Long.class.getName(),
                Float.class.getName(),
                Double.class.getName(),
                BigDecimal.class.getName(),
                BigInteger.class.getName(),
                AtomicInteger.class.getName(),
                Number.class.getName()
        );

        /**
         * 判断是否是基本数字类型
         * @param resolvedType
         * @return
         */
        public static boolean isAssignableBy(ResolvedType resolvedType){
            if(resolvedType instanceof ReferenceTypeImpl){
                return IDS.contains(((ReferenceTypeImpl) resolvedType).getId());
            }
            return false;
        }

    }

}
