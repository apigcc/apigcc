package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.google.common.collect.Sets;

import java.util.*;

public class AstUtils {


    /**
     * 获取注解表达式中，各个属性的值
     * @param n
     * @return
     */
    public static Map<String,Object> parseAtts(AnnotationExpr n){
        Map<String,Object> attrs = new HashMap<>();
        if(n instanceof SingleMemberAnnotationExpr){
            SingleMemberAnnotationExpr singleMemberAnnotationExpr = (SingleMemberAnnotationExpr) n;
            if(singleMemberAnnotationExpr.getMemberValue() instanceof StringLiteralExpr){
                StringLiteralExpr stringLiteralExpr = (StringLiteralExpr) singleMemberAnnotationExpr.getMemberValue();
                attrs.put("value",stringLiteralExpr.asString());
            }
        }else if(n instanceof NormalAnnotationExpr){
            NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) (n);
            normalAnnotationExpr.getPairs().forEach(ne -> {
                if(ne.getValue() instanceof StringLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((StringLiteralExpr)ne.getValue()).getValue());
                }else if(ne.getValue() instanceof IntegerLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((IntegerLiteralExpr)ne.getValue()).getValue());
                }else if(ne.getValue() instanceof DoubleLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((DoubleLiteralExpr)ne.getValue()).getValue());
                }else if(ne.getValue() instanceof LongLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((LongLiteralExpr)ne.getValue()).getValue());
                }else if(ne.getValue() instanceof BooleanLiteralExpr){
                    attrs.put(ne.getNameAsString(), ((BooleanLiteralExpr)ne.getValue()).getValue());
                }else{
                    attrs.put(ne.getNameAsString(), ne.getValue().toString());
                }
            });
        }
        return attrs;
    }

    public static final Set<String> baseTypeSet = Sets.newHashSet(
            Byte.class.getSimpleName(),
            Short.class.getSimpleName(),
            Integer.class.getSimpleName(),
            Long.class.getSimpleName(),
            Float.class.getSimpleName(),
            Double.class.getSimpleName(),
            Character.class.getSimpleName(),
            Boolean.class.getSimpleName(),
            byte.class.getName(),
            short.class.getName(),
            int.class.getName(),
            long.class.getName(),
            float.class.getName(),
            double.class.getName(),
            char.class.getName(),
            boolean.class.getName(),
            String.class.getSimpleName());

    /**
     * 是否基本类型 和 String
     * @param n
     * @return
     */
    public static boolean isBaseType(ClassOrInterfaceType n){
        return isBaseType(n.getNameAsString());
    }

    public static boolean isBaseType(String name){
        return baseTypeSet.contains(name);
    }

    /**
     * 获取常见类型的属性的默认值
     * @param type
     * @return
     */
    public static Object defaultValue(String type){
        if("String".equals(type)){
            return "";
        }
        if(baseTypeSet.contains(type)){
            return 0;
        }
        return "";
    }

    /**
     * 获取类的全限定名
     * @param n
     * @return
     */
    public static String getFullName(ClassOrInterfaceDeclaration n){
        if (n.getParentNode().isPresent() && n.getParentNode().get() instanceof CompilationUnit) {
            CompilationUnit cu = ((CompilationUnit) n.getParentNode().get());
            if(cu.getPackageDeclaration().isPresent()){
                String packageName = cu.getPackageDeclaration().get().getNameAsString();
                return packageName + "." + n.getNameAsString();
            }
        }
        return n.getNameAsString();
    }

    /**
     * 是否包含某个注解
     * @param n
     * @param annotations
     * @return
     */
    public static boolean isAnyPresent(NodeWithAnnotations n, String ... annotations){
        if(annotations==null){
            return false;
        }
        for (int i = 0; i < annotations.length; i++) {
            if(n.isAnnotationPresent(annotations[i])){
                return true;
            }
        }
        return false;
    }

    public static final Set<String> collectionTypeSet = Sets.newHashSet(
            Collection.class.getSimpleName(),
            List.class.getSimpleName(),
            Set.class.getSimpleName());

    /**
     * 是否为集合类型
     * @param name
     * @return
     */
    public static boolean isCollection(String name){
        return collectionTypeSet.contains(name);
    }


}
