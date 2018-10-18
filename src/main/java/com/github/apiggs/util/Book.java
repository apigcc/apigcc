package com.github.apiggs.util;

import java.util.ArrayList;

/**
 * 一种可翻 可直接看的集合
 */
public class Book<T> extends ArrayList<T>{

    boolean multi;

    public T get() {
        if(size()>0){
            return super.get(0);
        }
        return null;
    }
    public String getString(){
        T v = get();
        if(v!=null){
            return String.valueOf(v);
        }
        return "";
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public String getString(int index){
        return String.valueOf(get(index));
    }

}
