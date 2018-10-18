package com.github.apiggs.example.advanced;

import com.github.apiggs.example.common.BaseController;
import com.github.apiggs.example.common.ResultData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
