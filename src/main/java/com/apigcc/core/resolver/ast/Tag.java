package com.apigcc.core.resolver.ast;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Tag{

    String name;
    String key;
    String content;
    Map<String, String> inline = new HashMap<>();

}
