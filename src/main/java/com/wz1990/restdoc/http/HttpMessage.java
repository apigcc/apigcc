package com.wz1990.restdoc.http;

import com.wz1990.restdoc.schema.Node;
import lombok.Getter;
import lombok.Setter;

/**
 * An class that defines a HTTP message,
 * providing common properties and method
 */
@Getter
@Setter
public class HttpMessage extends Node{

    HttpVersion version;
    HttpRequest request = new HttpRequest();
    HttpResponse response = new HttpResponse();

}
