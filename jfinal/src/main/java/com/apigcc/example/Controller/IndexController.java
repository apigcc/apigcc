package com.apigcc.example.Controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller {

    public void index(){
        renderText("hello world");
    }
}
