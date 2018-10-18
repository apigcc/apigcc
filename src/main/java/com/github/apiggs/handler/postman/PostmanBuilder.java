package com.github.apiggs.handler.postman;

import com.github.apiggs.handler.postman.schema.*;
import com.github.apiggs.http.HttpMessage;
import com.github.apiggs.schema.Group;
import com.github.apiggs.schema.Tree;
import com.github.apiggs.util.ObjectMappers;
import com.github.apiggs.Environment;
import com.github.apiggs.handler.TreeHandler;
import com.github.apiggs.http.HttpHeaders;
import com.github.apiggs.schema.Cell;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Postman v2.1 json文件构建
 */
public class PostmanBuilder implements TreeHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(Tree tree, Environment env) {
        try {
            Postman postman = buildPostman(tree);
            File outFile = new File(env.getOut() + "/" + env.getProject() + ".json");
            FileUtils.writeStringToFile(outFile, ObjectMappers.toPretty(postman));
        } catch (IOException e) {
            log.error("build postman.json fail:" + e.getMessage(), e);
        }

    }

    private Postman buildPostman(Tree tree) {
        Postman postman = new Postman();
        Info info = new Info();
        info.set_postman_id(tree.getId());
        info.setName(tree.getName());
        info.setDescription(tree.getDescription());
        postman.setInfo(info);

        for (Group group : tree.getGroups()) {
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
            for (Cell cell : httpMessage.getRequest().getCells()) {
                request.getUrl().getQuery().add(Parameter.of(cell));
            }
        } else if (HttpHeaders.ContentType.APPLICATION_JSON.equals(httpMessage.getRequest().getHeaders().getContentType())) {
            request.getBody().setMode(BodyMode.raw);
            request.getBody().setRaw(ObjectMappers.toPretty(httpMessage.getRequest().getBody()));
        } else if (HttpHeaders.ContentType.APPLICATION_X_WWW_FORM_URLENCODED.equals(httpMessage.getRequest().getHeaders().getContentType())) {
            request.getBody().setMode(BodyMode.urlencoded);
            for (Cell cell : httpMessage.getRequest().getCells()) {
                request.getBody().getUrlencoded().add(Parameter.of(cell));
            }
        } else if (HttpHeaders.ContentType.MULTIPART_FORM_DATA.equals(httpMessage.getRequest().getHeaders().getContentType())) {
            request.getBody().setMode(BodyMode.formdata);
            for (Cell cell : httpMessage.getRequest().getCells()) {
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
