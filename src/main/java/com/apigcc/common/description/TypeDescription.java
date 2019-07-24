package com.apigcc.common.description;

import com.apigcc.schema.Row;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
public abstract class TypeDescription {

    protected String key;
    protected String type;
    protected String condition;
    protected String remark;
    protected Object value;

    public boolean isAvailable() {
        return !isUnAvailable();
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

    public Collection<Row> rows() {
        return Lists.newArrayList(new Row(key, type, condition, value==null?"":String.valueOf(value), remark));
    }

}
