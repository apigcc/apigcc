package com.github.apiggs.handler.postman.schema;


import com.github.apiggs.util.Cell;

public class Parameter{

    String key;
    String type;
    Object value;
    String description;
    boolean disabled = false;

    public static Parameter of(Cell<String> cell) {
        Parameter parameter = new Parameter();
        parameter.setKey(cell.get(0));
        parameter.setType(cell.get(1));
        parameter.setValue(cell.get(2));
        parameter.setDescription(cell.get(3));
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
