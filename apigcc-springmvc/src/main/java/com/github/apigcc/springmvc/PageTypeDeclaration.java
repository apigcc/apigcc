package com.github.apigcc.springmvc;

import com.github.apigcc.core.declaration.CodeTypeDeclaration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PageTypeDeclaration implements CodeTypeDeclaration {

    public static final String ID = "org.springframework.data.domain.Page";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public Path path() {
        return Paths.get("org/springframework/data/domain/Page.java");
    }

    @Override
    public String code() {
        return code;
    }

    String code = "package org.springframework.data.domain;\n" +
            "\n" +
            "import java.util.List;\n" +
            "\n" +
            "public class Page<T> {\n" +
            "\n" +
            "    /**\n" +
            "     * 当前页码\n" +
            "     */\n" +
            "    int number;\n" +
            "\n" +
            "    /**\n" +
            "     * 页内数据个数\n" +
            "     */\n" +
            "    int size;\n" +
            "\n" +
            "    /**\n" +
            "     * 总数据量\n" +
            "     */\n" +
            "    int totalElements;\n" +
            "\n" +
            "    /**\n" +
            "     * 总页数\n" +
            "     */\n" +
            "    int totalPages;\n" +
            "\n" +
            "    /**\n" +
            "     * 是否第一页\n" +
            "     */\n" +
            "    boolean first;\n" +
            "\n" +
            "    /**\n" +
            "     * 是否最后一页\n" +
            "     */\n" +
            "    boolean last;\n" +
            "\n" +
            "    /**\n" +
            "     * 是否空页\n" +
            "     */\n" +
            "    boolean empty;\n" +
            "\n" +
            "    /**\n" +
            "     * 数据\n" +
            "     */\n" +
            "    List<T> content;\n" +
            "\n" +
            "}\n";
}
