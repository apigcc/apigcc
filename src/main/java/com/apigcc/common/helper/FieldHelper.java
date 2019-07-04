package com.apigcc.common.helper;

import com.apigcc.common.description.ITypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.logic.AbstractClassDeclaration;

import java.util.ArrayList;
import java.util.List;

import static com.apigcc.common.ResolvedTypes.pick;

public class FieldHelper {

    /**
     * 通过access方法，获取属性名
     * @param methodName access方法名
     * @return 属性名
     */
    public static String getByAccessMethod(String methodName){
        if(methodName.startsWith("is") && methodName.length()>2){
            String first = methodName.substring(2, 3);
            String less = methodName.substring(3);
            return first.toLowerCase() + less;
        }
        if(methodName.startsWith("get") && methodName.length()>3){
            String first = methodName.substring(3, 4);
            String less = methodName.substring(4);
            return first.toLowerCase() + less;
        }
        return null;
    }

    /**
     * 获取可访问的属性
     * 1、通过get方法能访问的数据
     * 2、通过is方法能访问的数据
     * 3、被lombok标记Getter的属性  TODO 暂时获取所有属性
     * 4、被Getter标记的类
     * @param referenceType
     * @return
     */
//    public static List<ResolvedField> getAccessFields(ResolvedReferenceType referenceType){
//        List<ResolvedField> result = new ArrayList<>();
//        ResolvedReferenceTypeDeclaration typeDeclaration = referenceType.getTypeDeclaration();
//        if(typeDeclaration instanceof AbstractClassDeclaration){
//            AbstractClassDeclaration classDeclaration = (AbstractClassDeclaration) typeDeclaration;
//
//            for (MethodUsage it : classDeclaration.getAllMethods()) {
//                if(!it.getDeclaration().isStatic() &&
//                        !it.getDeclaration().isAbstract() &&
//                        !it.getQualifiedSignature().startsWith("java") &&
//                        AccessSpecifier.PUBLIC.equals(it.getDeclaration().accessSpecifier())){
//                    String fieldName = FieldHelper.getByAccessMethod(it.getName());
//                    if(fieldName != null){
//                        TypeDescription description = pick(it.getDeclaration().getReturnType());
//                        description.setRemark(CommentHelper.getComment(it));
//                        result.add(new ResolvedField(fieldName, description));
//                    }
//                }
//            }
//            for (ResolvedFieldDeclaration it : classDeclaration.getAllFields()) {
//                if(!it.isStatic()){
//                    ITypeDescription description = pick(it.getType());
//                    description.setRemark(CommentHelper.getComment(it));
//                    result.add(new ResolvedField(it.getName(), description));
//                }
//            }
//        }
//
//        return result;
//    }

}
