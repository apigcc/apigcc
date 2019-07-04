package com.apigcc.common.description;

import com.apigcc.common.ObjectMappers;
import com.apigcc.schema.Row;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * java类型描述
 */
@Setter
@Getter
public class ITypeDescription {

    public static final int TYPE_PRIMITIVE = 1;
    public static final int TYPE_STRING = 2;
    public static final int TYPE_ARRAY = 3;
    public static final int TYPE_BEAN = 4;

    int type;

    Number primitive;
    CharSequence charSequence;
    ArrayNode arrayNode;
    ObjectNode objectNode;

    List<Row> rows = new ArrayList<>();

    public void number(Number number){
        this.type = TYPE_PRIMITIVE;
        this.primitive = number;
        this.rows.add(new Row(number.getClass().getSimpleName()));
    }

    public void charSequence(CharSequence charSequence){
        this.type = TYPE_STRING;
        this.charSequence = charSequence;
        this.rows.add(new Row(charSequence.getClass().getSimpleName()));
    }

    public void array(ITypeDescription description){
        this.type = TYPE_ARRAY;
        if(this.arrayNode==null){
            this.arrayNode = ObjectMappers.instance.createArrayNode();
        }
        switch (description.type){
            case TYPE_PRIMITIVE:
                addNumber(arrayNode, description.getPrimitive());
                break;
            case TYPE_STRING:
                arrayNode.add(description.getCharSequence().toString());
                break;
            case TYPE_ARRAY:
                arrayNode.add(description.getArrayNode());
                description.fieldTypeSuffix("[]");
                break;
            case TYPE_BEAN:
                arrayNode.add(description.getObjectNode());
                break;
        }
        this.rows.addAll(description.rows);
    }

    public void object(String fieldName, ITypeDescription description) {
        this.type = TYPE_BEAN;
        if (this.objectNode==null) {
            this.objectNode = ObjectMappers.instance.createObjectNode();
        }
        switch (description.type){
            case TYPE_PRIMITIVE:
                setNumber(objectNode, fieldName, description.getPrimitive());
                description.fieldName(fieldName);
                break;
            case TYPE_STRING:
                this.objectNode.put(fieldName, description.getCharSequence().toString());
                description.fieldName(fieldName);
                break;
            case TYPE_ARRAY:
                this.objectNode.set(fieldName, description.getArrayNode());
                description.fieldName(fieldName);
                description.fieldTypeSuffix("[]");
                break;
            case TYPE_BEAN:
                this.objectNode.set(fieldName, description.getObjectNode());
                description.fieldName(fieldName);
                break;
        }
        this.rows.addAll(description.rows);
    }

    public static void setNumber(ObjectNode objectNode, String fieldName, Number number){
        if (number instanceof Byte) {
            objectNode.put(fieldName, number.byteValue());
        }else if(number instanceof Short){
            objectNode.put(fieldName, number.shortValue());
        }else if(number instanceof Integer){
            objectNode.put(fieldName, number.intValue());
        }else if(number instanceof Long){
            objectNode.put(fieldName, number.longValue());
        }else if(number instanceof Float){
            objectNode.put(fieldName, number.floatValue());
        }else if(number instanceof Double){
            objectNode.put(fieldName, number.doubleValue());
        }else{
            objectNode.putPOJO(fieldName, number);
        }
    }

    public static void addNumber(ArrayNode arrayNode, Number number){
        if (number instanceof Byte) {
            arrayNode.add(number.byteValue());
        }else if(number instanceof Short){
            arrayNode.add(number.shortValue());
        }else if(number instanceof Integer){
            arrayNode.add(number.intValue());
        }else if(number instanceof Long){
            arrayNode.add(number.longValue());
        }else if(number instanceof Float){
            arrayNode.add(number.floatValue());
        }else if(number instanceof Double){
            arrayNode.add(number.doubleValue());
        }else{
            arrayNode.addPOJO(number);
        }
    }

    public boolean isPrimitive(){
        return TYPE_PRIMITIVE == type;
    }

    public boolean isString(){
        return TYPE_STRING == type;
    }

    public boolean isArray(){
        return TYPE_ARRAY == type;
    }

    public boolean isBean(){
        return TYPE_BEAN == type;
    }

    public void fieldName(String key) {
        for (Row row : rows) {
            if (row.getKey()==null) {
                row.setKey(key);
            }else{
                row.setKey(key + "." + row.getKey());
            }
        }
    }

    public void fieldTypeSuffix(String suffix){
        for (Row row : rows) {
            if (row.getType()!=null) {
                row.setType(row.getType() + suffix);
            }
        }
    }

    /**
     * 设置字段备注
     * @param content
     */
    public void setRemark(String content) {
        if(content!=null && this.rows.size()==1){
            this.rows.get(0).setRemark(content);
        }
    }
}
