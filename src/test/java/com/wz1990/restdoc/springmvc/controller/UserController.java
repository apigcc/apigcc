package com.wz1990.restdoc.springmvc.controller;

import com.wz1990.restdoc.common.ResultData;
import com.wz1990.restdoc.common.User;
import com.wz1990.restdoc.common.UserDTO;
import com.wz1990.restdoc.common.UserQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户模块
 * 用户模块详情
 */
@RestController
@RequestMapping("/users")
public class UserController {

    public static final String name = "";


    /**
     *
     * 用户详情信息
     *
     * 主动根据id获取用户的信息
     * 非常方便的方法
     * @param id 用户编号
     * @return
     */
    @GetMapping(value = "/users/{id}")
    public ResultData<User> detail(@PathVariable String id){
        User user = new User();
        return ResultData.ok(user);
    }

    /**
     * 用户信息新增
     * @param user 用户信息
     * @return
     */
    @PostMapping("/users")
    public ResultData add(@RequestBody UserDTO user){
        return ResultData.ok();
    }

    /**
     * 用户列表信息查询
     * @param userQuery 查询参数
     * @return
     */
    @RequestMapping("/users/list")
    public ResultData list(UserQuery userQuery){
        return ResultData.ok();
    }

    /**
     * 用户列表信息搜索 中文
     * @param userQuery 查询参数
     * @return
     */
    @PostMapping("/users/search")
    public ResultData search(UserQuery userQuery){
        return ResultData.ok();
    }

    /**
     * 用户信息删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/{id}",method = RequestMethod.DELETE)
    public ResultData delete(@PathVariable String id){
        return ResultData.ok();
    }

    /**
     * 用户地址信息更新
     * @param address
     * @return
     */
    @RequestMapping(value = "/users/{id}/address",method = RequestMethod.PUT)
    public ResultData address(@RequestBody List<String> address){
        return ResultData.ok();
    }

    /**
     * 兴趣
     * @param intersting
     * @return
     */
    @RequestMapping(value = "/users/{id}/intersting",method = RequestMethod.PUT)
    public User intersting(@RequestBody List<Integer> intersting){
        return new User();
    }

    /**
     * arr
     * @param arr
     * @return
     */
    @RequestMapping(value = "/users/{id}/arr",method = RequestMethod.PUT)
    public ResultData arr(@RequestBody String[] arr){
        return ResultData.ok();
    }

}
