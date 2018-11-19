package com.apigcc.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2018/11/19.
 * 描述：
 */
@Getter
@Setter
@AllArgsConstructor
public class User {

    private Long id;

    private String name;

    private int age;

    private Date birth;

    private Map site;

    private List<Map<String,Object>> mapList;


}
