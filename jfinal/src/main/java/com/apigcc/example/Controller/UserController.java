package com.apigcc.example.Controller;

import com.jfinal.core.Controller;

/**
 * Created by lenovo on 2018/11/19.
 * 描述：
 */
public class UserController extends Controller {

    public void index(){
        System.out.println("==");
        renderText("hello world user");
    }
}
