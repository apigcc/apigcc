package com.wz1990.restdoc.springmvc.controller;

import com.wz1990.restdoc.common.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public interface AuthController {

    @GetMapping
    Auth auth();

}
