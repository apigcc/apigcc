package com.github.ayz6uem.restdoc;

import com.github.ayz6uem.restdoc.springmvc.SpringMvcVisitor;

/**
 * mvc框架判断，获取
 */
public enum Framework {

    SPRINGMVC(SpringMvcVisitor.class);

    public Class<? extends RestArrayVisitor> visitor;

    Framework(Class<? extends RestArrayVisitor> visitor) {
        this.visitor = visitor;
    }

    /**
     * 通过某种方法判断当前项目采用了什么框架
     * @return
     */
    public static Framework currentFramework(){
        return SPRINGMVC;
    }
}
