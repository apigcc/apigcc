package com.apigcc.core.common.helper;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.type.Type;

import java.util.Optional;

public class TypeNameHelper {

    public static String getName(Type type){
        String name = type.toString();
        if(type.isClassOrInterfaceType()){
            name = type.asClassOrInterfaceType().getNameAsString();
        }
        Optional<CompilationUnit> optional = CompilationUnitHelper.getCompilationUnit(type);
        if(optional.isPresent()){
            CompilationUnit compilationUnit = optional.get();
            return getNameFromImport(name, compilationUnit);
        }
        return name;
    }

    private static String getNameFromImport(String name, CompilationUnit compilationUnit){
        int dotPos = name.indexOf('.');
        String prefix = null;
        if (dotPos > -1) {
            prefix = name.substring(0, dotPos);
        }
        for (ImportDeclaration importDecl : compilationUnit.getImports()) {
            if (!importDecl.isAsterisk()) {
                String qName = importDecl.getNameAsString();
                boolean defaultPackage = !importDecl.getName().getQualifier().isPresent();
                boolean found = !defaultPackage && importDecl.getName().getIdentifier().equals(name);
                if (!found) {
                    if (prefix != null) {
                        found = qName.endsWith("." + prefix);
                        if (found) {
                            qName = qName + name.substring(dotPos);
                        }
                    }
                }
                if (found) {
                    return qName;
                }
            }
        }
        return name;
    }

}
