package com.github.ayz6uem.apiggy.handler;

import com.github.ayz6uem.apiggy.Environment;
import com.github.ayz6uem.apiggy.schema.Tree;

/**
 * 文档结构树访问器
 */
public interface TreeHandler {

    void handle(Tree tree, Environment env);

}
