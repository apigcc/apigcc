package com.github.ayz6uem.restdoc.common;

import lombok.Data;

@Data
public class UserQuery extends Page {

    String name;

    Auth auth;

}
