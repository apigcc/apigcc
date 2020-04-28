package com.github.apigcc.core.common.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Folder {

    String name;
    String description;
    List<Folder> item = new ArrayList<>();

}
