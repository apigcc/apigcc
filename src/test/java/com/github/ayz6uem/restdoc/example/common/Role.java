package com.github.ayz6uem.restdoc.example.common;

/**
 * 用户角色
 */
public enum Role {

    ADMIN("管理员"),USER("用户"),VIP("会员");

    String text;

    Role(String text) {
        this.text = text;
    }
}
