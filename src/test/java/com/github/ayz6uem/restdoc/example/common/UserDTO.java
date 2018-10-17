package com.github.ayz6uem.restdoc.example.common;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserDTO {

    /**
     * 编号
     */
    Integer id;
    /*姓名*/
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

    Map<String,String> attrs;

    User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String[] getIcons() {
        return icons;
    }

    public void setIcons(String[] icons) {
        this.icons = icons;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }
}
