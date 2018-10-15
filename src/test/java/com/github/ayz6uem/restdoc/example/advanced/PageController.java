package com.github.ayz6uem.restdoc.example.advanced;

import com.github.ayz6uem.restdoc.example.common.BaseController;
import com.github.ayz6uem.restdoc.example.hello.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/page")
public class PageController extends BaseController{

    /**
     * 默认页面，由于不是restful的，restdoc将忽略该Endpoint
     * @return
     */
    @GetMapping
    public ModelAndView index(){
        return new ModelAndView();
    }

    /**
     * Hello with ResponseBody
     * 由于带有@ResponseBody，restdoc将解析该Endpoint
     * @return
     */
    @GetMapping("/hello")
    @ResponseBody
    public Greeting hello(){
        return new Greeting(1,"hello world");
    }

}
