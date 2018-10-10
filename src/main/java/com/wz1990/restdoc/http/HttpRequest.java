package com.wz1990.restdoc.http;

import com.wz1990.restdoc.schema.Cell;
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

    String body;

    List<Cell> cells = new ArrayList<>();

    public void setUri(String groupPath, String path){

    }

}
