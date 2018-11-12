package com.apigcc.http;

import com.apigcc.common.Cell;
import com.apigcc.common.ObjectMappers;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class HttpResponse {

    HttpResponseStatus status = HttpResponseStatus.DEFAULT;
    HttpHeaders headers = new HttpHeaders();
    Object body;

    List<Cell<String>> cells = new ArrayList<>();

    public boolean isEmpty(){
        return cells.isEmpty() && Objects.isNull(body)  && headers.isEmpty();
    }

    public boolean hasBody(){
        return body != null;
    }

    public String bodyString(){
        if(getBody()!=null && getBody() instanceof JsonNode){
            return ObjectMappers.toPretty(getBody());
        }
        return "";
    }

}
