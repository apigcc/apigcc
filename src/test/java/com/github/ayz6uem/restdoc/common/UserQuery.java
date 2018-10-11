package com.github.ayz6uem.restdoc.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserQuery extends Page {

    String name;

    Auth auth;

}
