package com.apigcc.core.visitor;

import com.apigcc.core.resolver.ast.Comments;
import com.apigcc.core.resolver.ast.Tag;
import com.apigcc.core.resolver.ast.Tags;
import com.apigcc.core.schema.Appendix;
import com.apigcc.core.schema.Node;
import com.apigcc.core.schema.Tree;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Objects;

/**
 * 解析源码树
 */
public abstract class NodeVisitor extends VoidVisitorAdapter<Node> {

    /**
     * 一个java文件是一个CompilationUnit
     * 判断是否为一个Spring、JFinal环境
     * @param cu
     * @return
     */
    public abstract boolean accept(CompilationUnit cu);

    @Override
    public void visit(final CompilationUnit n, final Node arg) {
        n.getImports().forEach(p -> p.accept(this, arg));
        n.getModule().ifPresent(l -> l.accept(this, arg));
        n.getPackageDeclaration().ifPresent(l -> l.accept(this, arg));
        n.getTypes().forEach(p -> {
            if(Comments.notIgnore(p)){
                p.accept(this, arg);
            }
        });
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration n, final Node arg) {
        n.getExtendedTypes().forEach(p -> p.accept(this, arg));
        n.getImplementedTypes().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getMembers().forEach(p -> {
            if(Comments.notIgnore(p)){
                p.accept(this, arg);
            }
        });
        n.getName().accept(this, arg);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
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
                    Appendix appendix = Appendix.parse(n);
                    if(appendix!=null){
                        tree.getAppendices().add(appendix);
                    }
                }
            }
        }
        super.visit(n, arg);
    }

}
