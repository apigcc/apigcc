package com.github.apiggs.ast.extend;

/**
 * 扩展java的注释标签
 * 如 @index 等
 */
public enum DocTag {

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
    ignore

}
