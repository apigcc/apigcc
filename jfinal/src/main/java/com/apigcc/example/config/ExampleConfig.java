package com.apigcc.example.config;

import com.apigcc.example.Controller.IndexController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;

public class ExampleConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        /*开发模式*/
        me.setDevMode(true);
        me.setBaseViewPath("/");
    }

    /**
     * 路由配置
     * @param me
     */
    @Override
    public void configRoute(Routes me) {
        me.add("/", IndexController.class);
    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
