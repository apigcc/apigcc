package com.apigcc.http;

import com.apigcc.common.Cell;
import com.apigcc.common.ObjectMappers;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * a http request
 */
@Setter
@Getter
public class HttpRequest {

    HttpRequestMethod method;
    List<String> uris = new ArrayList<>();
    HttpHeaders headers = new HttpHeaders();

    Object body;

    List<Cell<String>> cells = new ArrayList<>();

    public Object queryString() {
        if (HttpRequestMethod.GET.equals(method)) {
            if(cells.size()>0){
                String queryString = join(cells);
                if(queryString.length()>0){
                    return "?" + queryString;
                }
            }
        }
        return "";
    }

    public boolean hasBody() {
        return !HttpRequestMethod.GET.equals(method) && (body != null || hasParameter());
    }

    public String bodyString() {
        if (getBody() != null && getBody() instanceof JsonNode) {
            return ObjectMappers.toPretty(getBody());
        } else {
            return join(cells);
        }
    }

    public boolean hasParameter() {
        for (Cell cell : cells) {
            if (cell.isEnable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据请求方法，参数等
     * 设置合理的content-Type
     * 最低是 form_urlencoded，如果已经是json，并不会覆盖
     * @return
     */
    public void checkContentType() {
        if (hasBody()) {
            headers.setContentType(HttpHeaders.ContentType.APPLICATION_X_WWW_FORM_URLENCODED);
        }
    }

    private String join(List<Cell<String>> cells){
        StringBuilder sb = new StringBuilder();
        for (Cell<String> cell : cells) {
            if(!cell.isEnable()){
                continue;
            }
            if(cell.size()<3){
                continue;
            }
            if(sb.length()>0){
                sb.append("&");
            }
            sb.append(cell.get(0)).append("=").append(cell.get(3));
        }
        return sb.toString();
    }

}
