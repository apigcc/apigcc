package com.github.ayz6uem.restdoc.http;

import com.github.ayz6uem.restdoc.schema.Cell;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class HttpResponse {

    HttpResponseStatus status = HttpResponseStatus.DEFAULT;
    HttpHeaders headers = new HttpHeaders();
    Object body;

    List<Cell> cells = new ArrayList<>();

    public boolean isEmpty(){
        return cells.isEmpty() && Objects.isNull(body)  && headers.isEmpty();
    }

}
