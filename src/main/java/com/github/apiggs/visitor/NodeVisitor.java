package com.github.apiggs.visitor;

import com.github.apiggs.Apiggs;
import com.github.apiggs.ast.*;
import com.github.apiggs.schema.Appendix;
import com.github.apiggs.schema.Node;
import com.github.apiggs.schema.Tree;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Objects;

/**
 * 解析源码树
 */
public abstract class NodeVisitor extends VoidVisitorAdapter<Node> {

    protected Apiggs context;

    public void setContext(Apiggs context) {
        this.context = context;
    }

    @Override
    public void visit(JavadocComment n, Node arg) {
        if (arg instanceof Tree) {
            Tree tree = (Tree) arg;
            Comments javadoc = Comments.of(n);
            //解析部分自定义标签
            for (Tag tag : javadoc.getTags()) {
                if (Objects.equals(tag.getName(), Tags.readme.name())) {
                    tree.setReadme(tag.getContent());
                }
                if (Objects.equals(tag.getName(), Tags.title.name())) {
                    tree.setName(tag.getContent());
                }
                if (Objects.equals(tag.getName(), Tags.description.name())) {
                    tree.setDescription(tag.getContent());
                }
                if (Objects.equals(tag.getName(), Tags.code.name())) {
                    Appendix appendix = parseCode(n);
                    if(appendix!=null){
                        tree.getAppendices().add(appendix);
                    }
                }
            }
        }
        super.visit(n, arg);
    }

    private Appendix parseCode(JavadocComment n) {
        if (n.getCommentedNode().isPresent()) {
            if (n.getCommentedNode().get() instanceof EnumDeclaration) {
                return Enums.toDetails((EnumDeclaration) n.getCommentedNode().get());
            }
            if (n.getCommentedNode().get() instanceof ClassOrInterfaceDeclaration) {
                return Fields.getConstants((ClassOrInterfaceDeclaration)n.getCommentedNode().get());
            }
        }
        return null;
    }

}
