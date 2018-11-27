package com.apigcc.core.visitor.jfinal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/11/27.
 * 描述：
 */
@Data
@NoArgsConstructor
public class RC {

    private String path;
    private String clazz;

    private String method;

    public RC(String path,String clazz){
        this.path=path;
        this.clazz=clazz;
    }

    private static List<RC> rcs=null;

    public static List<RC> getInstance(){
        if(rcs==null){
            rcs=new ArrayList<RC>();
        }
        return rcs;
    }
}
