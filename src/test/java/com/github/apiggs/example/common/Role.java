package com.github.apiggs.example.common;

/**
 * 用户角色
 * @code
 */
public enum Role {

    ADMIN("管理员"),USER("用户"),VIP("会员");

    String text;

    Role(String text) {
        this.text = text;
    }
}
