package com.apigcc.core.handler.postman;

import com.apigcc.core.Options;
import com.apigcc.core.handler.TreeHandler;
import com.apigcc.core.handler.postman.schema.*;
import com.apigcc.core.http.HttpHeaders;
import com.apigcc.core.http.HttpMessage;
import com.apigcc.core.schema.Group;
import com.apigcc.core.schema.Tree;
import com.apigcc.core.common.Cell;
import com.apigcc.core.common.ObjectMappers;
import com.apigcc.core.common.loging.Logger;
import com.apigcc.core.common.loging.LoggerFactory;
import com.apigcc.handler.postman.schema.*;
import com.core.handler.postman.schema.*;
import com.google.common.base.Charsets;

import java.nio.file.Path;

/**
 * Postman v2.1 json文件构建
 */
public class PostmanTreeHandler implements TreeHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(Tree tree, Options options) {
        Postman postman = buildPostman(tree);
        Path file = options.getOutPath().resolve(options.getId() + ".json");
        write(file, ObjectMappers.toPretty(postman), Charsets.UTF_8);
        log.info("Build {}", file);
    }

    private Postman buildPostman(Tree tree) {
        Postman postman = new Postman();
        Info info = new Info();
        info.set_postman_id(tree.getId());
        info.setName(tree.getName());
        info.setDescription(tree.getDescription());
        postman.setInfo(info);

        for (Group group : tree.getBucket().getGroups()) {
            Folder folder = new Folder();
            folder.setName(group.getName());
            folder.setDescription(group.getDescription());
            for (HttpMessage httpMessage : group.getNodes()) {
                folder.getItem().add(buildItem(httpMessage));
            }
            postman.getItem().add(folder);
        }

        return postman;
    }

    private Item buildItem(HttpMessage httpMessage) {
        Item item = new Item();
        item.setName(httpMessage.getName());
        item.setDescription(httpMessage.getDescription());

        Request request = new Request();
        request.setDescription(httpMessage.getDescription());
        request.getUrl().setPath(httpMessage.getRequest().getUris().get(0));
        request.setMethod(Method.of(httpMessage.getRequest().getMethod()));
        httpMessage.getRequest().getHeaders().forEach((key, value) -> request.getHeader().add(new Header(key, value)));
        if (Method.GET.equals(request.getMethod())) {
            for (Cell<String> cell : httpMessage.getRequest().getCells()) {
                request.getUrl().getQuery().add(Parameter.of(cell));
            }
        } else if (HttpHeaders.ContentType.APPLICATION_JSON.equals(httpMessage.getRequest().getHeaders().getContentType())) {
            request.getBody().setMode(BodyMode.raw);
            request.getBody().setRaw(ObjectMappers.toPretty(httpMessage.getRequest().getBody()));
        } else if (HttpHeaders.ContentType.APPLICATION_X_WWW_FORM_URLENCODED.equals(httpMessage.getRequest().getHeaders().getContentType())) {
            request.getBody().setMode(BodyMode.urlencoded);
            for (Cell<String> cell : httpMessage.getRequest().getCells()) {
                request.getBody().getUrlencoded().add(Parameter.of(cell));
            }
        } else if (HttpHeaders.ContentType.MULTIPART_FORM_DATA.equals(httpMessage.getRequest().getHeaders().getContentType())) {
            request.getBody().setMode(BodyMode.formdata);
            for (Cell<String> cell : httpMessage.getRequest().getCells()) {
                request.getBody().getFormdata().add(Parameter.of(cell));
            }
        }
        item.setRequest(request);

        Response response = new Response();
        response.setName(httpMessage.getResponse().getStatus().toString());
        response.setOriginalRequest(request);
        httpMessage.getResponse().getHeaders().forEach((key, value) -> response.getHeader().add(new Header(key, value)));
        response.setBody(ObjectMappers.toPretty(httpMessage.getResponse().getBody()));
        response.setCode(httpMessage.getResponse().getStatus().code());
        response.setStatus(httpMessage.getResponse().getStatus().reasonPhrase());
        item.getResponse().add(response);

        return item;
    }


}
