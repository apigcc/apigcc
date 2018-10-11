package com.github.ayz6uem.restdoc.postman.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Info {

    @JsonProperty("_postman_id")
    String id;
    String name;
    String description;
    String version;
    String schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

}
