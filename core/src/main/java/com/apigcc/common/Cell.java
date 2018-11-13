package com.apigcc.common;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 多个数据的组合
 */
public class Cell<T> {

    private List<T> values;

    private boolean enable;

    @SafeVarargs
    public Cell(T ... values) {
        this(true,values);
    }

    @SafeVarargs
    public Cell(boolean enable, T ... values) {
        this(enable,Lists.newArrayList(values));
    }

    public Cell(boolean enable, List<T> values) {
        this.values = values;
        this.enable = enable;
    }

    public List<T> toList(){
        return values;
    }

    public boolean isEnable() {
        return enable;
    }

    public void add(T value){
        values.add(value);
    }

    public int size(){
        return values.size();
    }

    public void set(int index, T t){
        values.set(index, t);
    }

    public T get(int index){
        return values.get(index);
    }

    public Cell<T> duplicate(){
        return new Cell<>(isEnable(),Lists.newArrayList(values));
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
