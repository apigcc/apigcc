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
     * 一个项目可以有多个桶(bucket)
     * 默认桶的名字为 ''
     */
    bucket,
    /**
     * 属性的模拟值
     */
    value;

    public boolean equals(Tag tag){
        return name().equals(tag.getName());
    }

}
