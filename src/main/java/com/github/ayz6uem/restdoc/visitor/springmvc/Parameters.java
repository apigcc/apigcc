package com.github.ayz6uem.restdoc.visitor.springmvc;

import com.github.ayz6uem.restdoc.ast.Annotations;
import com.github.ayz6uem.restdoc.ast.Comments;
import com.github.ayz6uem.restdoc.ast.ResolvedTypes;
import com.github.ayz6uem.restdoc.schema.Cell;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Spring Mvc 参数类型解析
 */
public class Parameters {

    Logger log = LoggerFactory.getLogger(this.getClass());

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
            setPrimitive(astResolvedType.primitive);
            setValue(astResolvedType.getValue());

            if (astResolvedType.primitive) {

                Cell cell = new Cell(expr.getNameAsString(), astResolvedType.name, astResolvedType.getValue());

                //解析RequestParam 获取字段名和默认值
                Object valueAttr = Annotations.getAttr(expr.getAnnotationByName(REQUEST_Param), "value");
                Object defaultValueAttr = Annotations.getAttr(expr.getAnnotationByName(REQUEST_Param), "defaultValue");
                if (valueAttr != null) {
                    cell.setName(String.valueOf(valueAttr));
                }
                if (defaultValueAttr != null) {
                    cell.setValue(defaultValueAttr);
                }

                cell.setDescription(Comments.getCommentFromMethod(expr));
                cells.add(cell);
            }
            cells.addAll(astResolvedType.cells);

        } catch (Exception e) {
            log.debug("try to resolve fail:" + expr.toString());
        }
    }


    public boolean isPrimitive() {
        return primitive;
    }

    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    public boolean isPathVariable() {
        return pathVariable;
    }

    public void setPathVariable(boolean pathVariable) {
        this.pathVariable = pathVariable;
    }

    public boolean isRequestBody() {
        return requestBody;
    }

    public void setRequestBody(boolean requestBody) {
        this.requestBody = requestBody;
    }

    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public boolean isMvc() {
        return mvc;
    }

    public void setMvc(boolean mvc) {
        this.mvc = mvc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
