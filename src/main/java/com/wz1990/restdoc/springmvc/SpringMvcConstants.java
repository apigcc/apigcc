package com.wz1990.restdoc.springmvc;

import java.util.Arrays;
import java.util.List;

public class SpringMvcConstants {

    public static final String CONTROLLER = "Controller";
    public static final String REST_CONTROLLER = "RestController";

    public static final String GET_MAPPING = "GetMapping";
    public static final String POST_MAPPING = "PostMapping";
    public static final String PUT_MAPPING = "PutMapping";
    public static final String DELETE_MAPPING = "DeleteMapping";
    public static final String REQUEST_MAPPING = "RequestMapping";

    public static final List<String> MAPPINGS = Arrays.asList(GET_MAPPING,POST_MAPPING,PUT_MAPPING,DELETE_MAPPING,REQUEST_MAPPING);


}
