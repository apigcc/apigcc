package com.github.apiggs.ast;

/**
 * 扩展java的注释标签
 * 如 @index 等
 */
public enum Tags {

    /**
     * Controller 顺序
     */
    index,
    title,
    description,
    readme,
    /**
     * 响应码
     */
    code,
    /**
     * 忽略该controller，该方法，该属性
     * 忽略子属性
     */
    ignore,
    /**
     * controller分组，将生成分组的文档
     */
    group

}
