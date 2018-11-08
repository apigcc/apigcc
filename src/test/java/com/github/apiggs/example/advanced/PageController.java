package com.github.apiggs.example.advanced;

import com.github.apiggs.example.common.*;
import com.github.apiggs.example.hello.Greeting;
import com.github.apiggs.model.Info;
import com.github.apiggs.model.InfoQuery;
import org.jruby.ir.Tuple;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @index 4
 */
@Controller
@RequestMapping("/page")
public class PageController extends BaseController {

    /**
     * 默认页面，由于不是restful的，restdoc将忽略该Endpoint
     *
     * @return
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView();
    }

    /**
     * Hello with ResponseBody
     *  *********
     * 由于带有@ResponseBody，restdoc将解析该Endpoint
     * <p>
     * hhh
     * \*********
     *  *********
     * hhhh
     * *********
     * <p>
     * class ************** {
     * <p>
     * }
     *
     * @return
     */
    @GetMapping("/hello")
    @ResponseBody
    public Greeting hello() {
        return new Greeting(1, "hello world");
    }


    /**
     * 未知的多泛型的tuple 演示
     *
     * @return
     */
    @GetMapping("/tuple")
    @ResponseBody
    public Tuple<UserDTO, User> tuple() {
        return null;
    }

    /**
     * 多个RequestMethod
     *
     * @return
     */
    @RequestMapping(value = "/multiMethod", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public ResultData multiMethod() {
        return null;
    }

    @PostMapping("/multi")
    @ResponseBody
    public ResultData<Wrapper<UserDTO>> multi(@RequestBody ResultData<Wrapper<List<UserDTO>>> resultData) {
        return null;
    }


    /**
     * 引用二方Jar
     * 使用二方Jar的类时，代码解析器无法获取类上的注释，注解
     * 只能获取属性的名称和类型
     * @param infoQuery
     * @return
     */
    @PostMapping("/jar")
    @ResponseBody
    public Info jar(@RequestBody InfoQuery infoQuery){
        return null;
    }


    /**
     * 一个复杂的类型 List<Map<String,User>>
     * @return
     */
    @GetMapping("/map")
    @ResponseBody
    public List<Map<String,User>> map(){
        return null;
    }

    /**
     * 一个更复杂的类型 List<Map<String,ResultData<Map<Integer,User>>>>
     * @return
     */
    @GetMapping("/map")
    @ResponseBody
    public List<Map<String,ResultData<Map<Integer,User>>>> maps(){
        return null;
    }

    /**
     * 一个问号类型 List<Map<String,List<?>>>
     * @return
     */
    @GetMapping("/map")
    @ResponseBody
    public List<Map<String,List<?>>> maps1(){
        return null;
    }
}
