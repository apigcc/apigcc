package com.github.ayz6uem.restdoc.http;

import com.github.ayz6uem.restdoc.schema.Node;
import lombok.Getter;
import lombok.Setter;

/**
 * An class that defines a HTTP message,
 * providing common properties and method
 */
@Getter
@Setter
public class HttpMessage extends Node {

    HttpVersion version = HttpVersion.DEFAULT;
    HttpRequest request = new HttpRequest();
    HttpResponse response = new HttpResponse();

}
