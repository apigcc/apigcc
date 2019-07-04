package com.apigcc.common.postman;

import lombok.extern.slf4j.Slf4j;

/**
 * Postman v2.1 json文件构建
 */
@Slf4j
public class PostmanTreeHandler {

//    @Override
//    public void handle(Tree tree, Options options) {
//        Postman postman = buildPostman(tree);
//        Path file = options.getOutPath().resolve(options.getId() + ".json");
//        write(file, ObjectMappers.pretty(postman), Charsets.UTF_8);
//        log.info("Build {}", file);
//    }
//
//    private Postman buildPostman(Tree tree) {
//        Postman postman = new Postman();
//        Info info = new Info();
//        info.set_postman_id(tree.getId());
//        info.setName(tree.getName());
//        info.setDescription(tree.getDescription());
//        postman.setInfo(info);
//
//        for (Group group : tree.getBucket().getGroups()) {
//            Folder folder = new Folder();
//            folder.setName(group.getName());
//            folder.setDescription(group.getDescription());
//            for (HttpMessage httpMessage : group.getNodes()) {
//                folder.getItem().add(buildItem(httpMessage));
//            }
//            postman.getItem().add(folder);
//        }
//
//        return postman;
//    }
//
//    private Item buildItem(HttpMessage httpMessage) {
//        Item item = new Item();
//        item.setName(httpMessage.getName());
//        item.setDescription(httpMessage.getDescription());
//
//        Request request = new Request();
//        request.setDescription(httpMessage.getDescription());
//        request.getUrl().setPath(httpMessage.getRequest().getUris().get(0));
//        request.setMethod(Method.of(httpMessage.getRequest().getMethod()));
//        httpMessage.getRequest().getHeaders().forEach((key, value) -> request.getHeader().add(new Header(key, value)));
//        if (Method.GET.equals(request.getMethod())) {
//            for (Cell<String> cell : httpMessage.getRequest().getCells()) {
//                request.getUrl().getQuery().add(Parameter.of(cell));
//            }
//        } else if (HttpHeaders.ContentType.APPLICATION_JSON.equals(httpMessage.getRequest().getHeaders().getContentType())) {
//            request.getBody().setMode(BodyMode.raw);
//            request.getBody().setRaw(ObjectMappers.pretty(httpMessage.getRequest().getBody()));
//        } else if (HttpHeaders.ContentType.APPLICATION_X_WWW_FORM_URLENCODED.equals(httpMessage.getRequest().getHeaders().getContentType())) {
//            request.getBody().setMode(BodyMode.urlencoded);
//            for (Cell<String> cell : httpMessage.getRequest().getCells()) {
//                request.getBody().getUrlencoded().add(Parameter.of(cell));
//            }
//        } else if (HttpHeaders.ContentType.MULTIPART_FORM_DATA.equals(httpMessage.getRequest().getHeaders().getContentType())) {
//            request.getBody().setMode(BodyMode.formdata);
//            for (Cell<String> cell : httpMessage.getRequest().getCells()) {
//                request.getBody().getFormdata().add(Parameter.of(cell));
//            }
//        }
//        item.setRequest(request);
//
//        Response response = new Response();
//        response.setName(httpMessage.getResponse().getStatus().toString());
//        response.setOriginalRequest(request);
//        httpMessage.getResponse().getHeaders().forEach((key, value) -> response.getHeader().add(new Header(key, value)));
//        response.setBody(ObjectMappers.pretty(httpMessage.getResponse().getBody()));
//        response.setCode(httpMessage.getResponse().getStatus().code());
//        response.setStatus(httpMessage.getResponse().getStatus().reasonPhrase());
//        item.getResponse().add(response);
//
//        return item;
//    }


}
