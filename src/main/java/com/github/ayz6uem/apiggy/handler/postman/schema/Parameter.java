package com.github.ayz6uem.apiggy.handler.postman.schema;


import com.github.ayz6uem.apiggy.schema.Cell;

public class Parameter{

    String key;
    String type;
    Object value;
    String description;
    boolean disabled = false;

    public static Parameter of(Cell cell) {
        Parameter parameter = new Parameter();
        parameter.setKey(cell.getName());
        parameter.setType(cell.getType());
        parameter.setValue(cell.getValue());
        parameter.setDescription(cell.getDescription());
        return parameter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
