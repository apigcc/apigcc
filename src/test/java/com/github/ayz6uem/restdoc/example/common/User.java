package com.github.ayz6uem.restdoc.example.common;

import java.util.List;

public class User {

    int id;
    String name;
    Integer age;

    User user;

    UserDTO userDTO;

    List<User> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
