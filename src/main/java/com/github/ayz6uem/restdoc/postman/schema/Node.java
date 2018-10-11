package com.github.ayz6uem.restdoc.postman.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Node {

    String id;
    String name;
    String description;


}
