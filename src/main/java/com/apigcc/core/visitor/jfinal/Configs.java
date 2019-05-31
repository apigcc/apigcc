package com.apigcc.core.visitor.jfinal;


import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2018/11/19.
 * 描述：
 */
public class Configs {

    public static final String CONFIG="JFinalConfig";
    public static final String ROUTE="configRoute";

    public static final String CONTROLLER="Controller";

    public static final List<String> ANNOTATIONS = Arrays.asList(CONFIG,CONTROLLER,ROUTE);

    public static final List<RC> RCS=RC.getInstance().getRcs();

    public static boolean accept(NodeList<ClassOrInterfaceType> nodes){
        for (int i = 0; i < nodes.size(); i++) {
            if(acceptConfig(nodes.get(i))){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }



//
//    public static boolean accept(ClassOrInterfaceType n){
//        if(!ANNOTATIONS.contains(n.getNameAsString())){
//            return Boolean.FALSE;
//        }
//        return Boolean.TRUE;
//    }
//


    public static boolean acceptConfig(ClassOrInterfaceType n){
        if(CONFIG.equals(n.getNameAsString())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static boolean acceptController(ClassOrInterfaceType n){
        if(CONTROLLER.equals(n.getNameAsString())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }



    public static boolean acceptRoute(ClassOrInterfaceType n){
        if(ROUTE.equals(n.getNameAsString())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }



}
