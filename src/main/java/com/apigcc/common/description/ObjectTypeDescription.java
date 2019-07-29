package com.apigcc.common.description;

import com.apigcc.common.ObjectMappers;
import com.apigcc.schema.Row;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class ObjectTypeDescription extends TypeDescription {

    private ObjectNode value = ObjectMappers.instance.createObjectNode();

    protected List<TypeDescription> members = Lists.newArrayList();

    public void merge(ObjectTypeDescription other) {
        value.setAll(other.getValue());
        members.addAll(other.members);
    }

    public void add(TypeDescription component) {
        members.add(component);
        if (component.isPrimitive()) {
            primitive(component.asPrimitive());
        } else if (component.isString()) {
            value.put(component.getKey(), component.asString().getValue());
        } else if (component.isArray()) {
            value.set(component.getKey(), component.asArray().getValue());
        } else if (component.isObject()) {
            value.set(component.getKey(), component.asObject().getValue());
        }
    }

    public void primitive(PrimitiveTypeDescription typeDescription) {
        switch (typeDescription.getType()) {
            case "byte":
                value.put(typeDescription.getKey(), (byte) typeDescription.getValue());
                break;
            case "short":
                value.put(typeDescription.getKey(), (short) typeDescription.getValue());
                break;
            case "char":
                value.put(typeDescription.getKey(), (char) typeDescription.getValue());
                break;
            case "int":
                value.put(typeDescription.getKey(), (int) typeDescription.getValue());
                break;
            case "long":
                value.put(typeDescription.getKey(), (long) typeDescription.getValue());
                break;
            case "boolean":
                value.put(typeDescription.getKey(), (boolean) typeDescription.getValue());
                break;
            case "float":
                value.put(typeDescription.getKey(), (float) typeDescription.getValue());
                break;
            case "double":
                value.put(typeDescription.getKey(), (double) typeDescription.getValue());
                break;
        }
    }

    @Override
    public void setKey(String key) {
        super.setKey(key);
        String memberPrefix = fullKey();
        for (TypeDescription member : members) {
            if (member.isAvailable()) {
                member.setPrefix(memberPrefix);
            }
        }
    }

    @Override
    public void setPrefix(String prefix) {
        super.setPrefix(prefix);
        String memberPrefix = fullKey();
        for (TypeDescription member : members) {
            if (member.isAvailable()) {
                member.setPrefix(memberPrefix);
            }
        }
    }

    public ObjectNode getValue() {
        return value;
    }

    @Override
    public Collection<Row> rows() {
        Collection<Row> rows = super.rows();
        for (TypeDescription member : members) {
            if (member.isAvailable()) {
                rows.addAll(member.rows());
            }
        }
        return rows;
    }
}
