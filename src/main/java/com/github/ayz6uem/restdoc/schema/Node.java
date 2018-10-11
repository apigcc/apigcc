package com.github.ayz6uem.restdoc.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Node {

    String id;
    String name;
    String description;

    @JsonIgnore
    Node parent;

}
