package com.apigcc.example.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Wrapper<T> {

    String wrapper;

    T data;

}
