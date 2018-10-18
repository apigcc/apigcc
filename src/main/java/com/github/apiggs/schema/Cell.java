package com.github.apiggs.schema;

import java.util.Arrays;
import java.util.List;

/**
 * 域
 * 请求参数、返回参数等的描述
 */
public class Cell {

    /**
     * 域名称
     */
    String name;
    /**
     * 域类型
     */
    String type;
    /**
     * 域值，可以是数字、字符串、复杂类型、数组、集合
     */
    Object value;
    String description;
    boolean disabled;

    public Cell() {
    }

    public Cell(String name, String type, Object value){
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Cell(String name, String type){
        this(name, type, null);
    }

    public Cell(String name, String type, boolean disabled){
        this(name, type);
        this.disabled = disabled;
    }

    public List<String> toList() {
        return Arrays.asList(name, type, value==null?"":String.valueOf(value), description);
    }

    public static String join(List<Cell> cells){
        StringBuilder sb = new StringBuilder();
        for (Cell cell : cells) {
            if(sb.length()>0){
                sb.append("&");
            }
            sb.append(cell.getName()).append("=").append(cell.getValue());
        }
        return sb.toString();
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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
