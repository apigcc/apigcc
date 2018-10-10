package com.wz1990.restdoc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class Enviroment {

    public static final String DEFAULT_BUILD_PATH = "build";
    public static final String DEFAULT_ADOC_PATH = DEFAULT_BUILD_PATH + "/adoc";
    public static final String DEFAULT_RESTDOC_PATH = DEFAULT_BUILD_PATH + "/restdoc";
    public static final String DEFAULT_JSON_FILE = DEFAULT_BUILD_PATH + "/json/index.json";

    /**
     * source code folder
     */
    String source;
    String jsonFile = DEFAULT_JSON_FILE;
    String adocPath = DEFAULT_ADOC_PATH;
    String restdocPath = DEFAULT_RESTDOC_PATH;
    /**
     * 文档标题
     */
    String docTitle;
    /**
     * 文档描述
     */
    String docDescription;
    /**
     * 忽略哪些类型的参数、类解析
     */
    Set<String> ignoreType = new HashSet<>();

}
