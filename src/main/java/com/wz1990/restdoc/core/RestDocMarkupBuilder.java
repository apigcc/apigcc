package com.wz1990.restdoc.core;

import com.wz1990.restdoc.schema.Group;
import com.wz1990.restdoc.schema.Item;
import com.wz1990.restdoc.schema.Node;
import com.wz1990.restdoc.schema.Tree;
import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import io.github.swagger2markup.markup.builder.MarkupDocBuilders;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class RestDocMarkupBuilder {

    Tree tree;
    String path;
    MarkupDocBuilder builder;

    public RestDocMarkupBuilder(Tree tree, String path) {
        this.tree = tree;
        this.path = path;
        builder = MarkupDocBuilders.documentBuilder(MarkupLanguage.ASCIIDOC);
    }


    public void build() {
        builder.documentTitle(tree.getInfo().getName())
                .paragraph(tree.getInfo().getDescription());

        for (int i = 0; i < tree.getNodes().size(); i++) {
            Node node = tree.getNodes().get(i);
            buildNode(node);
        }

        Path indexFile = Paths.get(path+"/index");
        builder.writeToFile(indexFile, StandardCharsets.UTF_8);
    }

    private void buildNode(Node node){
        if(node instanceof Group){
            buildGroup((Group) node);
        }else if(node instanceof Item){
            buildItem((Item) node);
        }
    }

    private void buildGroup(Group group){
        builder.sectionTitleLevel(1,group.getName())
                .paragraph(group.getDescription());
        for (int i = 0; i < group.getNodes().size(); i++) {
            Node node = group.getNodes().get(i);
            buildNode(node);
        }
    }

    private void buildItem(Item item){
        builder.sectionTitleLevel(2,item.getName());
        if(Objects.nonNull(item.getDescription())){
            builder.paragraph(item.getDescription());
        }
        Item.Request request = item.getRequest();
        builder.boldText(request.getMethod().toString());
        builder.text(request.getUrl().toString());
        if(Objects.nonNull(request.getDescription())){
            builder.paragraph(request.getDescription());
        }
    }
}
