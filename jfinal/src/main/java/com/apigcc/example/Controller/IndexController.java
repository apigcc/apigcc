package com.apigcc.example.Controller;

import com.apigcc.example.model.User;
import com.jfinal.core.Controller;

import java.util.*;

/**
 * @author lenovo
 */
public class IndexController extends Controller {

    public void index(){
        System.out.println("==");
        renderText("hello world");
    }

    public void getJson(){
        Map<String,Object> map=new HashMap<>();
        map.put("class",5);
        map.put("site",6);
        List<Map<String,Object>> lists=new ArrayList<>();
        User user=new User(1L,"张三",11,new Date(),map,lists);
        renderJson(user);
    }

    public void getQrcode(){
        renderQrCode("http://www.baidu.com",300,400);
    }
}
