package com.github.apigcc.core.common.helper;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionClassDeclaration;
import com.github.javaparser.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class ClassHelper {

    /**
     * 获取并解析父类
     * @param n
     * @return
     */
    public static Optional<ClassOrInterfaceDeclaration> getParent(ClassOrInterfaceDeclaration n) {
        if (n.getExtendedTypes().isEmpty()) {
            return Optional.empty();
        }
        return tryToResolve(n.getExtendedTypes().get(0));
    }

    private static Optional<ClassOrInterfaceDeclaration> tryToResolve(ClassOrInterfaceType type) {
        try {
            ResolvedReferenceType resolvedReferenceType = type.resolve();
            if (resolvedReferenceType.getTypeDeclaration() instanceof JavaParserClassDeclaration) {
                JavaParserClassDeclaration typeDeclaration = (JavaParserClassDeclaration) resolvedReferenceType.getTypeDeclaration();
                return Optional.of(typeDeclaration.getWrappedNode());
            }
            if(resolvedReferenceType.getTypeDeclaration() instanceof ReflectionClassDeclaration){
                ReflectionClassDeclaration typeDeclaration = (ReflectionClassDeclaration) resolvedReferenceType.getTypeDeclaration();
                //TODO 解析反射得到的类
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return Optional.empty();
    }



    /**
     * 获取类型名称
     * @param type
     * @return
     */
    public static String getId(Type type){
        String name;
        if(type.isClassOrInterfaceType()){
            name = type.asClassOrInterfaceType().getNameAsString();
        }else{
            name = type.toString();
        }
        Optional<CompilationUnit> optional = CompilationUnitHelper.getCompilationUnit(type);
        if(optional.isPresent()){
            CompilationUnit compilationUnit = optional.get();
            return getNameFromImport(name, compilationUnit);
        }
        return name;
    }

    /**
     * 寻找import行，组装完整的类名称
     * @param name
     * @param compilationUnit
     * @return
     */
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

    /**
     * 获取泛型信息
     * @param referenceType
     * @param index 位置信息
     * @return
     */
    public static Optional<ResolvedType> getTypeParameter(ResolvedReferenceType referenceType, int index){
        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParameters = referenceType.getTypeParametersMap();
        if(typeParameters.size()>index){
            Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair = typeParameters.get(index);
            return Optional.of(pair.b);
        }
        return Optional.empty();
    }

    /**
     * 获取泛型信息
     * @param referenceType
     * @param a 如 T E 等
     * @return
     */
    public static Optional<ResolvedType> getTypeParameter(ResolvedReferenceType referenceType, String a){
        List<Pair<ResolvedTypeParameterDeclaration, ResolvedType>> typeParameters = referenceType.getTypeParametersMap();
        for (Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair : typeParameters) {
            if(Objects.equals(pair.a.getName(),a)){
                return Optional.of(pair.b);
            }
        }
        return Optional.empty();
    }

    /**
     * 使用父类的泛型 解析当前类
     * @param parent
     * @param field
     */
    public static ResolvedType useClassTypeParameter(ResolvedReferenceType parent, ResolvedReferenceType field){
        for (Pair<ResolvedTypeParameterDeclaration, ResolvedType> pair : field.getTypeParametersMap()) {
            if(pair.b.isTypeVariable()){
                Optional<ResolvedType> typeParameter = getTypeParameter(parent, pair.b.asTypeVariable().describe());
                if (typeParameter.isPresent()) {
                    return field.replaceTypeVariables(pair.b.asTypeVariable().asTypeParameter(), typeParameter.get());
                }
            }
        }
        return field;
    }

}
