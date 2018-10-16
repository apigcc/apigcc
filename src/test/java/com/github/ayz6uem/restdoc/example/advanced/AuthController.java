package com.github.ayz6uem.restdoc.example.advanced;

import com.github.ayz6uem.restdoc.example.common.BaseController;
import com.github.ayz6uem.restdoc.example.common.ResultData;
import com.github.ayz6uem.restdoc.example.hello.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @index 3
 */
@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController{

    /**
     *
     * @param token 上报的身份验证token，jwt
     * @return
     */
    @PostMapping
    public ResultData auth(@RequestHeader() String token){
        return ResultData.ok();
    }

}
