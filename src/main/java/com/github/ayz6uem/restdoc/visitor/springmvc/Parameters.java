package com.github.ayz6uem.restdoc.visitor.springmvc;

import com.github.ayz6uem.restdoc.ast.Annotations;
import com.github.ayz6uem.restdoc.ast.Comments;
import com.github.ayz6uem.restdoc.schema.Cell;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.ayz6uem.restdoc.ast.ResolvedTypes;
import com.google.common.collect.Sets;
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
    public static final String REQUEST_Param = "RequestParam";
    public static final String PATH_VARIABLE = "PathVariable";

    public static final String MVC_MODEL = "MODEL";

    public static final Set<String> MVCS = Sets.newHashSet(MVC_MODEL);

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

        if (expr.isAnnotationPresent(PATH_VARIABLE)) {
            parameters.setPathVariable(true);
            Cell cell = new Cell(expr.getNameAsString(), expr.getTypeAsString(), true);
            cell.setDescription(Comments.getCommentFromMethod(expr));
            parameters.cells.add(cell);
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
            ResolvedTypes astResolvedType = ResolvedTypes.of(resolvedType);
            setPrimitive(astResolvedType.isPrimitive());
            setValue(astResolvedType.getValue());

            if(astResolvedType.isPrimitive()){

                Cell cell = new Cell(expr.getNameAsString(), astResolvedType.getName(), astResolvedType.getValue());

                //解析RequestParam 获取字段名和默认值
                Object valueAttr = Annotations.getAttr(expr.getAnnotationByName(REQUEST_Param), "value");
                Object defaultValueAttr = Annotations.getAttr(expr.getAnnotationByName(REQUEST_Param), "defaultValue");
                if(valueAttr!=null){
                    cell.setName(String.valueOf(valueAttr));
                }
                if(defaultValueAttr!=null){
                    cell.setValue(defaultValueAttr);
                }

                cell.setDescription(Comments.getCommentFromMethod(expr));
                cells.add(cell);
            }
            cells.addAll(astResolvedType.getCells());

        } catch (Exception e) {
            log.debug("try to resolve fail:" + expr.toString());
        }
    }


}
