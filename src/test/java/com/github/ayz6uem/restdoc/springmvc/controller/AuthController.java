package com.github.ayz6uem.restdoc.springmvc.controller;

import com.github.ayz6uem.restdoc.common.Auth;
import com.github.ayz6uem.restdoc.common.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public interface AuthController {

    /**
     *
     * @param name 名称
     * @return
     */
    @GetMapping(headers = "content-Type=application/json")
    Auth auth(@RequestParam(value = "nickname", defaultValue = "ayz6uem") String name, @RequestParam int a);

    /**
     * 未知的类型
     * @return
     */
    @GetMapping("/unknow")
    ResponseEntity<User> unknowType(MultipartFile file);

}
