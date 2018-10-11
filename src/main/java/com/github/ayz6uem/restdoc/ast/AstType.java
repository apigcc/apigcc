package com.github.ayz6uem.restdoc.ast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.ayz6uem.restdoc.util.JsonHelper;
import com.github.javaparser.ast.type.TypeParameter;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.*;

@Data
public class AstType {

    String name;
    String fullName;
    LinkedList<Field> fields = new LinkedList<>();
    String parentName;
    Map<String, AstGeneric> genericMap = new HashMap<>();

    boolean primitive;
    boolean array;
    @JsonIgnore
    JsonNode node;
    @JsonIgnore
    ObjectNode jsonNode;



    public void addGeneric(TypeParameter typeParameter, int index) {
        AstGeneric astGeneric = new AstGeneric();
        astGeneric.setSymbol(typeParameter.getNameAsString());
        astGeneric.setIndex(index);
        getGenericMap().put(astGeneric.getSymbol(),astGeneric);
    }

    @JsonIgnore
    public boolean isGeneric(String symbol) {
        return genericMap.containsKey(symbol);
    }
    @JsonIgnore
    public boolean isGeneric(Field field) {
        return isGeneric(field.getType());
    }

    @JsonIgnore
    public Object getGeneric(String symbol) {
        return genericMap.get(symbol);
    }

    @Data
    public static class Field {
        private String name;
        private String type;
        private Object value;
        private String description;
    }

    public ObjectNode getJsonNode() {
        if (Objects.isNull(jsonNode)) {
            generateJson();
        }
        return jsonNode;
    }

    @JsonIgnore
    @SneakyThrows
    public String getPrettyJson() {
        return JsonHelper.toPretty(getJsonNode());
    }

    @SneakyThrows
    public void generateJson() {
        ObjectNode objectNode = JsonHelper.objectMapper.createObjectNode();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            JsonHelper.set(objectNode, field.getName(), field.getValue());
        }
        jsonNode = objectNode;
    }

}
