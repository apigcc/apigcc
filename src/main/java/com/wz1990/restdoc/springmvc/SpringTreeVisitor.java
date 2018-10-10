package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.wz1990.restdoc.RestDoc;
import com.wz1990.restdoc.ast.AstTypeUtils;
import com.wz1990.restdoc.http.HttpMessage;
import com.wz1990.restdoc.schema.Group;
import com.wz1990.restdoc.util.URL;

import java.util.Optional;

public class SpringTreeVisitor extends VoidVisitorAdapter<Void> {

    private RestDoc restDoc;
    private Group group;

    public SpringTreeVisitor() {
    }

    public SpringTreeVisitor(RestDoc restDoc) {
        this.restDoc = restDoc;
    }

    @Override
    public void visit(CompilationUnit n, Void arg) {
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        if (Controllers.accept(n.getAnnotations())) {
            String name = AstTypeUtils.getNameInScope(n);
            String fullName = AstTypeUtils.getFullName(n);
            group = new Group();
            group.setId(fullName);
            group.setName(name);
            Optional<AnnotationExpr> requestMapping = n.getAnnotationByName(RequestMappings.REQUEST_MAPPING);
            if (requestMapping.isPresent()) {
                RequestMappings requestMappings = RequestMappings.of(requestMapping.get());
                group.getExt().put("path", requestMappings.getPath());
                group.getExt().put("method", requestMappings.getMethod());
            }
            restDoc.getTree().getNodes().add(group);
        } else {
            group = null;
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        if (group != null && RequestMappings.accept(n.getAnnotations())) {
            HttpMessage message = new HttpMessage();
            message.setName(n.getNameAsString());
            message.setId(group.getId() + "." + message.getName());

            for (int i = 0; i < n.getAnnotations().size(); i++) {
                AnnotationExpr expr = n.getAnnotation(i);
                if(RequestMappings.accept(expr)){
                    RequestMappings requestMappings = RequestMappings.of(expr);
                    message.getRequest().setMethod(requestMappings.getMethod());
                    message.getRequest().setUri(URL.normalize(group.getExt().get("path"),requestMappings.getPath()));
                }
            }

        }
        super.visit(n, arg);
    }
}
