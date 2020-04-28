package com.github.apigcc.core.common.helper;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 解决循环依赖问题
 */
public class ReferenceContext {

    private static ThreadLocal<ReferenceContext> threadLocal = new ThreadLocal<>();

    public static ReferenceContext getInstance(){
        ReferenceContext context = threadLocal.get();
        if (context == null) {
            context = new ReferenceContext();
            threadLocal.set(context);
        }
        return context;
    }

    private final Set<Object> set = Sets.newHashSet();

    /**
     * 记录解析的值
     * @param object
     * @return true 成功 false 失败
     */
    public boolean push(Object object){
        return set.add(object);
    }

    public boolean remove(Object object){
        return set.remove(object);
    }

    public Set<Object> getValues(){
        return set;
    }

}
