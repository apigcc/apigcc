package com.apigcc.example.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class User {

    int id;
    @NotBlank
    String name;
    @Min(1)
    @NotNull
    Integer age;
    Date createAt;
    @NotBlank
    @JsonProperty("Sex")
    String sex;

    User user;

    UserDTO userDTO;

    List<User> users;

}
