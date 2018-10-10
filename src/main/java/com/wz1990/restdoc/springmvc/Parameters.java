package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.wz1990.restdoc.ast.AstUtils;
import com.wz1990.restdoc.schema.Cell;
import com.wz1990.restdoc.util.Collections;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Spring Mvc 参数类型解析
 */
@Getter
@Setter
public class Parameters {

    public static final String REQUEST_BODY = "RequestBody";
    public static final String PATH_VARIABLE = "PathVariable";

    public static final String MVC_MODEL = "MODEL";

    public static final Set<String> MVCS = Collections.set(MVC_MODEL);

    /**
     * 是否基本类型（包括String)
     */
    boolean baseType;
    /**
     * 是否集合（包括 数组)
     */
    boolean array;
    /**
     * 是否路径字段
     */
    boolean pathVariable;
    /**
     * 是否请求体接收字段
     */
    boolean requestBody;
    /**
     * 是否文件字段
     */
    boolean file;
    /**
     * 是否spring mvc 保留字段
     */
    boolean mvc;

    String name;
    String type;
    Object value;
    String description;
    List<Cell> cells = new ArrayList<>();

    public static Parameters of(Parameter expr){
        Parameters parameters = new Parameters();

        if(expr.isAnnotationPresent(PATH_VARIABLE)){
            parameters.setPathVariable(true);
        }
        if(expr.isAnnotationPresent(REQUEST_BODY)){
            parameters.setRequestBody(true);
        }

        if (expr.getType() instanceof ClassOrInterfaceType) {
            ClassOrInterfaceType type = (ClassOrInterfaceType) expr.getType();
            if (AstUtils.isBaseType(type)) {
                parameters.setBaseType(true);
            }
            if(MVCS.contains(type.getNameAsString())){
                parameters.setMvc(true);
            }
        }

        return parameters;
    }



}
