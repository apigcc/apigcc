package com.github.ayz6uem.restdoc.springmvc;

import com.github.ayz6uem.restdoc.schema.Cell;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.ayz6uem.restdoc.ast.ASTResolvedType;
import com.github.ayz6uem.restdoc.util.Collections;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Spring Mvc 参数类型解析
 */
@Getter
@Setter
@Slf4j
public class Parameters {

    public static final String REQUEST_BODY = "RequestBody";
    public static final String PATH_VARIABLE = "PathVariable";

    public static final String MVC_MODEL = "MODEL";

    public static final Set<String> MVCS = Collections.set(MVC_MODEL);

    /**
     * 是否基本类型(包括string)
     */
    boolean primitive;
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

    public static Parameters of(Parameter expr) {
        Parameters parameters = new Parameters();

        //TODO ignore file mvc ?

        //TODO 解析@RequestParam
        if (expr.isAnnotationPresent(PATH_VARIABLE)) {
            parameters.setPathVariable(true);
            parameters.cells.add(new Cell(expr.getNameAsString(), expr.getTypeAsString(), true));
        } else {
            if (expr.isAnnotationPresent(REQUEST_BODY)) {
                parameters.setRequestBody(true);
            }
            parameters.tryResolve(expr);
        }


        return parameters;
    }

    private void tryResolve(Parameter expr) {
        try {
            ResolvedParameterDeclaration parameterDeclaration = expr.resolve();
            ResolvedType resolvedType = parameterDeclaration.getType();
            ASTResolvedType astResolvedType = ASTResolvedType.of(resolvedType);
            setPrimitive(astResolvedType.isPrimitive());
            //TODO expr 的 value
            setValue(astResolvedType.getValue());
            if(astResolvedType.isPrimitive()){
                cells.add(new Cell(expr.getNameAsString(), astResolvedType.getName(), astResolvedType.getValue()));
            }
            cells.addAll(astResolvedType.getCells());

        } catch (Exception e) {
            log.warn("try to resolve fail:" + expr.toString());
        }
    }


}
