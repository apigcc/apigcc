package com.wz1990.restdoc.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest api schema from postman schema
 * https://schema.getpostman.com/json/collection/v2.1.0/collection.json
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Tree {

    Info info = new Info();

    @JsonProperty("item")
    List<Node> nodes = new ArrayList<>();
}
