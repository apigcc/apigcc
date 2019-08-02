package com.apigcc.core.common.postman;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Info {

    String name;
    String _postman_id;
    String description;
    String version;
    String schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

}
