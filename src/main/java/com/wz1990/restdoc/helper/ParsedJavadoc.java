package com.wz1990.restdoc.helper;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ParsedJavadoc {

    String name;
    String description;
    Map<String,String> params = new HashMap<>();

}
