package com.github.ayz6uem.apiggy.example.advanced;

import com.github.ayz6uem.apiggy.example.common.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户模块（标题）
 * 用户示例模块文字描述（详情）
 * 支持多行文字
 * @index 2
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {

    /**
     * 用户详情信息
     * 主动根据id获取用户的信息
     *
     * @param id 用户编号
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultData<User> detail(@PathVariable String id) {
        User user = new User();
        return ResultData.ok(user);
    }

    /**
     * 用户详情信息（根据email或电话号码）
     * 多路径适配
     *
     * @param email
     * @param phone
     * @return
     */
    @GetMapping(value = {"/detail", "/info"})
    public ResultData<User> detailOrInfo(String email, String phone) {
        return ResultData.ok(new User());
    }

    /**
     * 用户信息新增
     *
     * @param user 用户信息
     * @return
     * @ignore user.id 忽略UserDTO中的id
     */
    @PostMapping
    public ResultData add(@RequestBody UserDTO user) {
        return ResultData.ok();
    }

    /**
     * 用户信息更新
     *
     * @param user 用户信息
     * @return
     */
    @PatchMapping
    public ResultData update(@RequestBody UserDTO user) {
        return ResultData.ok();
    }

    /**
     * 用户列表信息查询
     * 默认展示GET方法查询
     * 返回集合类的结果
     *
     * @param page 页码
     * @param size 每页条数
     * @return
     */
    @RequestMapping("/list")
    public ResultData<List<User>> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return ResultData.ok();
    }

    /**
     * 用户列表信息搜索
     * POST搜索时，请求参数将放在请求体中
     *
     * @param userQuery 查询参数
     * @return
     */
    @PostMapping("/search")
    public ResultData<List<User>> search(UserQuery userQuery) {
        return ResultData.ok();
    }

    /**
     * 用户信息删除
     * ResponseEntity、Model以及未知类型将忽略
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultData> delete(@PathVariable String id, Model model) {
        return ResponseEntity.ok(ResultData.ok());
    }

    /**
     * 用户禁用
     * 某些项目使用自定义的ArgumentResolver，让spring自动注入一些信息
     * restdoc在解析时，可通过env.ignoreTypes("UserDtails")来忽略这些
     *
     * @param userDetails 当前登录用户的信息
     * @return
     */
    @RequestMapping(value = "/{id}/disable", method = RequestMethod.PUT)
    public ResultData disable(UserDetails userDetails) {
        return ResultData.ok();
    }

    /**
     * 查询角色下的用户总数
     * @param role 枚举类型
     * @return
     */
    @GetMapping("/role")
    public ResultData<Integer> listFromRole(Role role){
        return ResultData.ok();
    }

    /**
     * 批量上传用户信息
     * @param list
     * @return
     */
    @PostMapping("/batch")
    public ResultData batch(@RequestBody List<UserDTO> list){
        return null;
    }

}
