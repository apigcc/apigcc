package com.apigcc.example.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Menu {

    int id;
    String name;
    List<Menu> menus;

}
