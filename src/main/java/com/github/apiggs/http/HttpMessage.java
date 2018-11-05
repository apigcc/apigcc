package com.github.apiggs.http;

import com.github.apiggs.schema.Group;
import com.github.apiggs.schema.Node;
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

    Group parent;

}
