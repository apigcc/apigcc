package com.github.apiggs.visitor;

import com.github.apiggs.visitor.jfinal.JFinalVisitor;
import com.github.apiggs.visitor.springmvc.SpringVisitor;
import com.github.javaparser.ast.CompilationUnit;

import java.util.Arrays;
import java.util.List;

/**
 * 判断当前项目使用了什么框架
 */
public enum Framework {

    SPRINGMVC(SpringVisitor.class),
    JFINAL(JFinalVisitor.class)
    ;

    private Class<? extends NodeVisitor> visitor;

    Framework(Class<? extends NodeVisitor> visitor) {
        this.visitor = visitor;
    }

    public NodeVisitor getVisitor() {
        try {
            return visitor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static Framework current;

    /**
     * 获取当前代码环境所使用的框架信息
     * @param cus
     * @return
     */
    public static Framework getCurrent(List<CompilationUnit> cus){
        if(current == null){
            current = findoutFramework(cus);
        }
        return current;
    }

    /**
     * 解析环境，找到对应的框架
     * @param results
     * @return
     */
    public static Framework findoutFramework(List<CompilationUnit> results){
        for (CompilationUnit cu : results) {
            for (Framework framework : values()) {
                if(framework.getVisitor().accept(cu)){
                    return framework;
                }
            }
        }
        throw new IllegalStateException("can not find any framework in project, only support "+ Arrays.toString(values()));
    }

}
