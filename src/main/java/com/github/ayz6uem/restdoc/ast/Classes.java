package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class Classes {

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
