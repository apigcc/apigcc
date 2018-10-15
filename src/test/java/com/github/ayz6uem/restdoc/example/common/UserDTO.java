package com.github.ayz6uem.restdoc.example.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class UserDTO {

    /**
     * 编号
     */
    Integer id;
    /*
     * 姓名
     */
    String name;
    //年龄
    int age;
    /**
     * 生日，还是推荐使用javadoc
     */
    Date birthday;
    /**
     * 用户标签
     */
    List<String> tags;
    /**
     * 用户图标
     */
    String[] icons;

}
