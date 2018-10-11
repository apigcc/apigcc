package com.github.ayz6uem.restdoc.http;

import com.github.ayz6uem.restdoc.schema.Cell;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * a http request
 */
@Getter
@Setter
public class HttpRequest {

    HttpRequestMethod method;
    String uri;
    HttpHeaders headers = new HttpHeaders();

    Object body;

    List<Cell> cells = new ArrayList<>();

}
