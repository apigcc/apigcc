package com.github.apigcc.core.description;

import com.github.apigcc.core.schema.NodeWithComment;
import com.github.apigcc.core.common.helper.StringHelper;
import com.github.apigcc.core.schema.Row;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * 解析后的类型描述
 */
@Setter
@Getter
public abstract class TypeDescription extends NodeWithComment {

    protected String prefix = "";
    protected String key = "";
    protected String type;
    protected StringBuilder condition = new StringBuilder();
    protected String remark;
    protected Object value;
    protected Object defaultValue;
    protected Boolean required;

    public boolean isAvailable() {
        return !isUnAvailable() && !isIgnore();
    }

    public boolean isUnAvailable() {
        return this instanceof UnAvailableTypeDescription;
    }

    public boolean isPrimitive() {
        return this instanceof PrimitiveTypeDescription;
    }

    public PrimitiveTypeDescription asPrimitive() {
        return (PrimitiveTypeDescription) this;
    }

    public boolean isString() {
        return this instanceof StringTypeDescription;
    }

    public StringTypeDescription asString() {
        return (StringTypeDescription) this;
    }

    public boolean isArray() {
        return this instanceof ArrayTypeDescription;
    }

    public ArrayTypeDescription asArray() {
        return (ArrayTypeDescription) this;
    }

    public boolean isObject() {
        return this instanceof ObjectTypeDescription;
    }

    public ObjectTypeDescription asObject() {
        return (ObjectTypeDescription) this;
    }

    /**
     * 添加注解
     * @param value
     */
    public void addRemark(String value){
        if(value==null){
            return;
        }
        if(remark==null){
            remark = value;
        }else{
            //TODO 换行符
            remark += " " + value;
        }
    }

    /**
     * 添加条件
     */
    public void addCondition(String condition){
        if(condition==null){
            return;
        }
        if(this.condition.length()==0){
            this.condition.append(condition);
        }else{
            //TODO 换行符
            this.condition.append(" ").append(condition);
        }
    }

    public void addConditions(Iterable<String> conditions){
        for (String condition : conditions) {
            this.addCondition(condition);
        }
    }

    public String fullKey(){
        return StringHelper.join(".",prefix,key);
    }

    public Collection<Row> rows() {
        String key = fullKey();
        if(StringHelper.isBlank(key)){
            return Lists.newArrayList();
        }
        String def;
        if(defaultValue!=null){
            def = String.valueOf(defaultValue);
        }else if(value!=null){
            def = String.valueOf(value);
        }else{
            def = "";
        }

        if(required!=null){
            condition.append("required=").append(required);
        }

        return Lists.newArrayList(new Row(key, type, condition.toString(), def, remark));
    }

    @Override
    public void setComment(String content) {
        this.addRemark(content);
    }
}
