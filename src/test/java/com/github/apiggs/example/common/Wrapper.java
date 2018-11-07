package com.github.apiggs.example.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Wrapper<T> {

    String wrapper;

    T data;

}
