package com.wz1990.restdoc.core;

import com.wz1990.restdoc.helper.JsonHelper;
import com.wz1990.restdoc.schema.Tree;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;

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
