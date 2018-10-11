package com.github.ayz6uem.restdoc;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.ayz6uem.restdoc.schema.Group;
import com.github.ayz6uem.restdoc.util.JavadocHelper;
import com.github.ayz6uem.restdoc.util.ParsedJavadoc;
import com.github.ayz6uem.restdoc.ast.AstUtils;

import java.util.Objects;

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
            group.setId(AstUtils.getFullName(n));
            ParsedJavadoc parsedJavadoc = JavadocHelper.parse(n.getJavadoc());
            group.setName(Objects.nonNull(parsedJavadoc.getName())?parsedJavadoc.getName():n.getNameAsString());
            if(Objects.nonNull(parsedJavadoc.getDescription())){
                group.setDescription(parsedJavadoc.getDescription());
            }
            restDoc.getTree().getNodes().add(group);

            visit(n,group);

//            n.getMembers().forEach(p -> p.accept(restArrayVisitor, group));
        }
        super.visit(n, restDoc);
    }

//    VoidVisitorAdapter<Group> restArrayVisitor = new VoidVisitorAdapter<Group>() {
//
//        @Override
//        public void visit(MethodDeclaration n, Group group) {
//            if (accept(n)) {
//                Item item = new Item();
//                item.setId(group.getId()+"."+n.getNameAsString());
//                ParsedJavadoc parsedJavadoc = JavadocHelper.parse(n.getJavadoc());
//                item.setName(Objects.nonNull(parsedJavadoc.getName())?parsedJavadoc.getName():n.getNameAsString());
//                if(Objects.nonNull(parsedJavadoc.getDescription())){
//                    item.getRequest().addDescription(parsedJavadoc.getDescription());
//                }
//
//                RestArrayVisitor.this.visit(n, item, parsedJavadoc);
//
//                group.getNodes().add(item);
//            }
//        }
//
//    };

    public abstract boolean accept(ClassOrInterfaceDeclaration n);

    public abstract boolean accept(MethodDeclaration n);

    public abstract void visit(ClassOrInterfaceDeclaration n, Group group);

//    public abstract void visit(MethodDeclaration n, Item item, ParsedJavadoc parsedJavadoc);

}
