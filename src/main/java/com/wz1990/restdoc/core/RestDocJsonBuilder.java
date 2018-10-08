package com.wz1990.restdoc.core;

import com.wz1990.restdoc.helper.JsonHelper;
import com.wz1990.restdoc.schema.Tree;
import lombok.SneakyThrows;

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
        File file = targetFile(jsonFile);
        JsonHelper.getPrettyObjectWriter().writeValue(file, tree);
    }

    private File targetFile(String jsonFile){
        File file = new File(jsonFile);
        if(!file.isDirectory()){
            return new File(file.getParent()+"/index.json");
        }
        return new File(jsonFile+"/index.json");
    }

}
