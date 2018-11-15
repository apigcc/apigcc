package com.apigcc.core.visitor.springmvc;

import com.apigcc.core.common.URL;
import com.apigcc.core.http.HttpHeaders;
import com.apigcc.core.http.HttpMessage;
import com.apigcc.core.http.HttpRequest;
import com.apigcc.core.http.HttpRequestMethod;
import com.apigcc.core.resolver.TypeResolvers;
import com.apigcc.core.resolver.Types;
import com.apigcc.core.resolver.ast.Clazz;
import com.apigcc.core.schema.Bucket;
import com.apigcc.core.schema.Group;
import com.apigcc.core.schema.Node;
import com.apigcc.core.schema.Tree;
import com.apigcc.core.visitor.NodeVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.Type;

/**
 * Spring endpoints解析
 */
public class SpringVisitor extends NodeVisitor {

    public static final String PACKAGE = "org.springframework";

    @Override
    public boolean accept(CompilationUnit cu) {
        return cu.getImports().stream()
                .anyMatch(importDeclaration -> importDeclaration.getNameAsString().startsWith(PACKAGE));
    }

    /**
     * 查找Endpoints接入类
     *
     * @param n
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Node arg) {
        if (arg instanceof Tree && Controllers.accept(n.getAnnotations())) {
            Tree tree = (Tree) arg;
            Group group = new Group();
            group.setId(Clazz.getFullName(n));
            group.setName(Clazz.getNameInScope(n));
            group.setRest(Controllers.isResponseBody(n));
            //解析注释
            group.accept(n.getComment());
            //获取往哪个桶里放
            Bucket bucket = tree.getBucket(group.getBucketName());
            group.setParent(bucket);
            bucket.getGroups().add(group);

            //path 和 method 影响方法的处理
            RequestMappings.of(n).ifPresent(requestMappings -> {
                group.getExt().put("path", requestMappings.getPath().get(0));
                group.getExt().put("method", requestMappings.getMethod());
            });

            arg = group;

        }
        super.visit(n, arg);
    }

    /**
     * 请求方法处理
     *
     * @param n
     * @param arg
     */
    @Override
    public void visit(MethodDeclaration n, Node arg) {
        if (arg instanceof Group && RequestMappings.accept(n.getAnnotations())) {
            Group group = (Group) arg;
            if (group.isRest() || RequestMappings.isRequestBody(n)) {
                //请求方法处理成HttpMessage
                HttpMessage message = new HttpMessage();
                message.setId(group.getId() + "." + message.getName());
                message.setName(n.getNameAsString());
                message.setParent(group);

                visit(n.getType(), message);
                n.getAnnotations().forEach(p -> visit(p, message));
                n.getParameters().forEach(p -> visit(p, message));

                //尝试从注释解析名称和描述
                message.accept(n.getComment());
                //设置为代码顺序
                message.setIndex(group.getNodes().size());

                group.getNodes().add(message);
            }

        }
        super.visit(n, arg);
    }

    /**
     * 解析方法返回值
     *
     * @param type
     * @param message
     */
    private void visit(Type type, HttpMessage message) {
        Types astResolvedType = TypeResolvers.of(type);
        if (astResolvedType.isResolved()) {
            message.getResponse().setBody(astResolvedType.getValue());
            message.getResponse().getCells().addAll(astResolvedType.getCells());
        }
    }

    /**
     * 解析方法参数
     * 有@RequestBody时，请求方法由GET改为POST，
     *
     * @param n
     * @param message
     */
    private void visit(Parameter n, HttpMessage message) {
        Parameters parameters = Parameters.of(n);

        HttpRequest request = message.getRequest();
        request.getCells().addAll(parameters.getCells());
        if (parameters.isFile()) {
            //File 修改请求头为 form data
            if (HttpRequestMethod.GET.equals(request.getMethod())) {
                request.setMethod(HttpRequestMethod.POST);
            }
            request.getHeaders().setContentType(HttpHeaders.ContentType.MULTIPART_FORM_DATA);
        } else if (parameters.isHeader()) {
            request.getHeaders().put(parameters.getName(), String.valueOf(parameters.getValue()));
        } else if (parameters.isRequestBody()) {
            //RequestBody 修改请求头为json
            if (HttpRequestMethod.GET.equals(request.getMethod())) {
                request.setMethod(HttpRequestMethod.POST);
            }
            request.getHeaders().setContentType(HttpHeaders.ContentType.APPLICATION_JSON);
            request.setBody(parameters.getValue());
        }
    }

    /**
     * 请求方法的注解处理
     *
     * @param n
     * @param message
     */
    private void visit(AnnotationExpr n, HttpMessage message) {
        if (!RequestMappings.accept(n)) {
            return;
        }
        Group group = message.getParent();
        RequestMappings requestMappings = RequestMappings.of(n);
        message.getRequest().setMethod(requestMappings.getMethod());
        message.getRequest().checkContentType();
        for (String path : requestMappings.getPath()) {
            message.getRequest().getUris().add(URL.normalize(group.getExt().get("path"), path));
        }
        message.getRequest().getHeaders().add(requestMappings.getHeaders());
    }


}
