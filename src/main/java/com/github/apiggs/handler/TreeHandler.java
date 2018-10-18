package com.github.apiggs.handler;

import com.github.apiggs.schema.Tree;
import com.github.apiggs.Environment;

/**
 * 文档结构树访问器
 */
public interface TreeHandler {

    void handle(Tree tree, Environment env);

}
