package com.github.ayz6uem.restdoc.postman;

import com.github.ayz6uem.restdoc.util.JsonHelper;
import com.github.ayz6uem.restdoc.schema.Tree;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class RestDocJsonBuilder {

    String jsonFile;
    Tree tree;

    public RestDocJsonBuilder(Tree tree, String jsonFile) {
        this.tree = tree;
        this.jsonFile = jsonFile;
    }

    @SneakyThrows
    public void build() {
        File file = new File(jsonFile);
        String json = JsonHelper.getPrettyObjectWriter().writeValueAsString(tree);
        FileUtils.writeStringToFile(file,json);
    }

}
