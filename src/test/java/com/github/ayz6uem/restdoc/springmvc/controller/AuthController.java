package com.github.ayz6uem.restdoc.springmvc.controller;

import com.github.ayz6uem.restdoc.common.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public interface AuthController {

    @GetMapping(headers = "content-Type=application/json")
    Auth auth();

}
