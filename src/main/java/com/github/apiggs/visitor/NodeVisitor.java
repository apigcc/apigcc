package com.github.apiggs.visitor;

import com.github.apiggs.Apiggs;
import com.github.apiggs.ast.Comments;
import com.github.apiggs.ast.Enums;
import com.github.apiggs.ast.extend.ExtendTag;
import com.github.apiggs.schema.Cell;
import com.github.apiggs.schema.Node;
import com.github.apiggs.schema.Tree;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

public abstract class NodeVisitor extends VoidVisitorAdapter<Node> {

    protected Apiggs context;

    public void setContext(Apiggs context) {
        this.context = context;
    }

    @Override
    public void visit(JavadocComment n, Node arg) {
        if(arg instanceof Tree){
            Tree tree = (Tree) arg;
            Comments javadoc = Comments.of(n);
            for (Comments.Tag tag : javadoc.tags) {
                if(Objects.equals(tag.name,ExtendTag.readme.name())){
                    tree.setReadme(tag.content);
                }
                if(Objects.equals(tag.name,ExtendTag.responseCode.name())){
                    tree.getResponseCode().addAll(parseResponseCode(n));
                }
            }
        }
        super.visit(n, arg);
    }

    private List<Cell> parseResponseCode(JavadocComment n){
        if(n.getCommentedNode().isPresent()){
            if(n.getCommentedNode().get() instanceof EnumDeclaration){
                return Enums.toDetails((EnumDeclaration) n.getCommentedNode().get());
            }
        }
        return Lists.newArrayList();
    }

}
