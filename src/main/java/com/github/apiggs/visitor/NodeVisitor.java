package com.github.apiggs.visitor;

import com.github.apiggs.Apiggs;
import com.github.apiggs.ast.Comments;
import com.github.apiggs.ast.Enums;
import com.github.apiggs.ast.Fields;
import com.github.apiggs.ast.Tags;
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
            for (Comments.Tag tag : javadoc.tags) {
                if (Objects.equals(tag.name, Tags.readme.name())) {
                    tree.setReadme(tag.content);
                }
                if (Objects.equals(tag.name, Tags.title.name())) {
                    tree.setName(tag.content);
                }
                if (Objects.equals(tag.name, Tags.description.name())) {
                    tree.setDescription(tag.content);
                }
                if (Objects.equals(tag.name, Tags.code.name())) {
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
