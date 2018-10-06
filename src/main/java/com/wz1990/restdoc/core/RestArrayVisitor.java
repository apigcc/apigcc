package com.wz1990.restdoc.core;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.wz1990.restdoc.ast.AstHelper;
import com.wz1990.restdoc.helper.JavadocHelper;
import com.wz1990.restdoc.helper.ParsedJavadoc;
import com.wz1990.restdoc.schema.Group;
import com.wz1990.restdoc.schema.Item;

public abstract class RestArrayVisitor extends VoidVisitorAdapter<RestDoc> {

    protected RestDoc restDoc;

    @Override
    public void visit(CompilationUnit n, RestDoc restDoc){
        this.restDoc = restDoc;
        super.visit(n, restDoc);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, RestDoc restDoc) {
        if (accept(n)) {
            Group group = new Group();
            group.setId(AstHelper.getFullName(n));
            ParsedJavadoc parsedJavadoc = JavadocHelper.parse(n.getJavadoc());
            group.setName(parsedJavadoc.getName());
            group.setDescription(parsedJavadoc.getDescription());
            restDoc.getTree().getNodes().add(group);
            n.getMembers().forEach(p -> p.accept(restArrayVisitor, group));
        }
        super.visit(n, restDoc);
    }

    VoidVisitorAdapter<Group> restArrayVisitor = new VoidVisitorAdapter<Group>() {

        @Override
        public void visit(MethodDeclaration n, Group group) {
            if (accept(n)) {
                Item item = new Item();
                item.setId(group.getId()+"."+n.getNameAsString());
                ParsedJavadoc parsedJavadoc = JavadocHelper.parse(n.getJavadoc());
                item.setName(parsedJavadoc.getName());
                item.getRequest().addDescription(parsedJavadoc.getDescription());

                RestArrayVisitor.this.visit(n, item, parsedJavadoc);

                group.getNodes().add(item);
            }
        }

    };

    public abstract boolean accept(ClassOrInterfaceDeclaration n);

    public abstract boolean accept(MethodDeclaration n);

    public abstract void visit(MethodDeclaration n, Item item, ParsedJavadoc parsedJavadoc);

}
