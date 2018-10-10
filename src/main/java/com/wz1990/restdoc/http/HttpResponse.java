package com.wz1990.restdoc.http;

import com.wz1990.restdoc.schema.Cell;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HttpResponse {

    HttpResponseStatus status;
    HttpHeaders headers = new HttpHeaders();
    String body;

    List<Cell> cells = new ArrayList<>();

}
