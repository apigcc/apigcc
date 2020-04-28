package com.github.apigcc.core.common.helper;

import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.Optional;

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

    public static Optional<Object> getInitializerValue(ResolvedFieldDeclaration declaredField){
        if(declaredField instanceof JavaParserFieldDeclaration){
            JavaParserFieldDeclaration field = (JavaParserFieldDeclaration) declaredField;
            return field.getVariableDeclarator().getInitializer().map(ExpressionHelper::getValue);
        }
        return Optional.empty();
    }

    /**
     * 获取真实的属性类型
     * @param parent
     * @param field
     * @return
     */
    public static ResolvedType getActuallyType(ResolvedReferenceType parent, ResolvedFieldDeclaration field) {
        ResolvedType type = field.getType();
        if (type.isReferenceType()) {
            //将父类的T，传递给 属性的T
            return ClassHelper.useClassTypeParameter(parent, type.asReferenceType());
        }
        if (type.isTypeVariable()) {
            //类型为T，这种泛型
            Optional<ResolvedType> optional = ClassHelper.getTypeParameter(parent, field.getType().asTypeParameter().getName());
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return type;
    }

}
