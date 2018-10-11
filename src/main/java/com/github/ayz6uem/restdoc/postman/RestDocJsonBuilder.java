package com.github.ayz6uem.restdoc.postman;

import com.github.ayz6uem.restdoc.schema.Tree;
import lombok.SneakyThrows;

public class RestDocJsonBuilder {

    String out;
    Tree tree;

    public RestDocJsonBuilder(Tree tree, String out) {
        this.tree = tree;
        this.out = out;
    }

    @SneakyThrows
    public void build() {
//        File file = new File(jsonFile);
//        String json = ObjectMappers.getPrettyObjectWriter().writeValueAsString(tree);
//        FileUtils.writeStringToFile(file,json);
    }

}
