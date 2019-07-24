package com.apigcc.common.description;

import com.apigcc.common.ObjectMappers;
import com.apigcc.schema.Row;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ArrayTypeDescription extends TypeDescription {

    protected ArrayNode value;
    protected TypeDescription component;

    public ArrayTypeDescription(TypeDescription component) {
        this.component = component;
        this.value = ObjectMappers.instance.createArrayNode();
        if(component.isAvailable()){
            this.type = component.getType() + "[]";
            if(component.isPrimitive()){
                primitive(component.asPrimitive());
            }else if(component.isString()){
                value.add(component.asString().getValue());
            }else if(component.isArray()){
                value.add(component.asArray().getValue());
            }else if(component.isObject()){
                value.add(component.asObject().getValue());
            }
        }else{
            this.type = "[]";
        }
    }

    public void primitive(PrimitiveTypeDescription typeDescription){
        switch (typeDescription.getType()){
            case "byte":
                value.add((byte)typeDescription.getValue());
                break;
            case "short":
                value.add((short)typeDescription.getValue());
                break;
            case "char":
                value.add((char)typeDescription.getValue());
                break;
            case "int":
                value.add((int)typeDescription.getValue());
                break;
            case "long":
                value.add((long)typeDescription.getValue());
                break;
            case "boolean":
                value.add((boolean)typeDescription.getValue());
                break;
            case "float":
                value.add((float)typeDescription.getValue());
                break;
            case "double":
                value.add((double)typeDescription.getValue());
                break;
        }
    }

    @Override
    public void setKey(String key) {
        super.setKey(key);
        if (component.isAvailable()) {
            component.setKey(key);
        }
    }

    public ArrayNode getValue(){
        return value;
    }

    @Override
    public Collection<Row> rows() {
        ArrayList<Row> rows = new ArrayList<>();
        if(key != null){
            rows.addAll(super.rows());
        }
        if(component.isAvailable()){
            rows.addAll(component.rows());
        }
        return rows;
    }
}
