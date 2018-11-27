package com.apigcc.core.visitor.jfinal;

import com.apigcc.core.common.loging.Logger;
import com.apigcc.core.common.loging.LoggerFactory;
import com.apigcc.core.visitor.springmvc.Parameters;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2018/11/19.
 * 描述：
 */
public class Configs {

    public static final String CONFIG="JFinalConfig";
    public static final String ROUTE="configRoute";
    public static final List<String> ANNOTATIONS = Arrays.asList(CONFIG);

    public static final List<RC> RCS=RC.getInstance();

    public static boolean accept(NodeList<ClassOrInterfaceType> nodes){
        for (int i = 0; i < nodes.size(); i++) {
            if(accept(nodes.get(i))){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static boolean accept(SimpleName name){
        if(ROUTE.contains(name.asString())){
            return Boolean.TRUE;
        }
        for (RC rc:RCS) {
           if(rc.getClazz().startsWith(name.asString())){
               //解析
               return Boolean.TRUE;
           }
        }


        return Boolean.FALSE;
    }


    public static boolean accept(ClassOrInterfaceType n){
        if(!ANNOTATIONS.contains(n.getNameAsString())){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }



}
