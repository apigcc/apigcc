package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.wz1990.restdoc.RestDoc;
import com.wz1990.restdoc.ast.AstTypeUtils;
import com.wz1990.restdoc.http.HttpHeaders;
import com.wz1990.restdoc.http.HttpMessage;
import com.wz1990.restdoc.schema.Cell;
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
            group.getNodes().add(message);

            n.getAnnotations().forEach(expr -> visitAnnotation(expr, message));
            n.getParameters().forEach(expr -> visitParameter(expr, message));
            visitType(n.getType(),message);
        }
        super.visit(n, arg);
    }

    private void visitAnnotation(AnnotationExpr n, HttpMessage message) {
        if (!RequestMappings.accept(n)) {
            return;
        }
        RequestMappings requestMappings = RequestMappings.of(n);
        message.getRequest().setMethod(requestMappings.getMethod());
        //根据method 设置默认content-Type
        message.getRequest().getHeaders().setContentType(requestMappings.getMethod());
        message.getRequest().setUri(URL.normalize(group.getExt().get("path"), requestMappings.getPath()));
        message.getRequest().getHeaders().add(requestMappings.getHeaders());
    }

    private void visitParameter(Parameter expr, HttpMessage message){
        //RequestBody 修改请求头为json
        Parameters parameters = Parameters.of(expr);
        if(parameters.isFile()){
            message.getRequest().getHeaders().setContentType(HttpHeaders.ContentType.MULTIPART_FORM_DATA);
        }
        if(parameters.isRequestBody()){
            message.getRequest().getHeaders().setContentType(HttpHeaders.ContentType.APPLICATION_JSON);
        }

        message.getRequest().getCells().addAll(parameters.getCells());

    }

    private void visitType(Type type, HttpMessage message){

    }

}
