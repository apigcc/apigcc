package com.github.apigcc.core.declaration;

import java.nio.file.Path;

/**
 * 通过字符串代码声明类型
 */
public interface CodeTypeDeclaration {

    /**
     * 类标识，全限定名
     * @return
     */
    String id();

    /**
     * 文件应该的目录
     * @return
     */
    Path path();

    /**
     * 代码
     * @return
     */
    String code();

}
