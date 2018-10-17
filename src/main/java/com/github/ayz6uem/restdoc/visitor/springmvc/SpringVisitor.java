package com.github.ayz6uem.restdoc.visitor.springmvc;

import com.github.ayz6uem.restdoc.ast.Classes;
import com.github.ayz6uem.restdoc.ast.Comments;
import com.github.ayz6uem.restdoc.ast.ResolvedTypes;
import com.github.ayz6uem.restdoc.http.HttpHeaders;
import com.github.ayz6uem.restdoc.http.HttpMessage;
import com.github.ayz6uem.restdoc.http.HttpRequest;
import com.github.ayz6uem.restdoc.http.HttpRequestMethod;
import com.github.ayz6uem.restdoc.schema.Group;
import com.github.ayz6uem.restdoc.schema.Node;
import com.github.ayz6uem.restdoc.schema.Tree;
import com.github.ayz6uem.restdoc.util.URL;
import com.github.ayz6uem.restdoc.visitor.NodeVisitor;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.Type;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Spring endpoints解析
 */
public class SpringVisitor extends NodeVisitor {

    /**
     * 查找Endpoints接入类
     *
     * @param n
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Node arg) {
        if (arg != null && arg instanceof Tree) {
            Tree tree = (Tree) arg;
            if (Controllers.accept(n.getAnnotations())) {
                String name = Classes.getNameInScope(n);
                String fullName = Classes.getFullName(n);
                Group group = new Group();
                group.setParent(tree);
                group.setId(fullName);
                group.setName(name);
                group.setRest(Controllers.isResponseBody(n));
                if(n.getComment().isPresent()){
                    Comments comments = Comments.of(n.getComment().get());
                    if(StringUtils.isNotEmpty(comments.name)){
                        group.setName(comments.name);
                    }
                    group.setDescription(comments.description);
                    group.setIndex(Comments.getIndexTag(n.getComment()));
                }
                //path 和 method 影响方法的处理
                Optional<RequestMappings> optional = RequestMappings.of(n);
                if (optional.isPresent()) {
                    group.getExt().put("path", optional.get().getPath().get(0));
                    group.getExt().put("method", optional.get().getMethod());
                }

                super.visit(n, group);

                if(!group.isEmpty()){
                    tree.getGroups().add(group);
                }
            }
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
        if (arg != null && arg instanceof Group && RequestMappings.accept(n.getAnnotations())) {
            Group group = (Group) arg;
            if(group.isRest() || RequestMappings.isRequestBody(n)){
                //请求方法处理成HttpMessage
                HttpMessage message = new HttpMessage();
                message.setParent(group);
                message.setName(n.getNameAsString());
                message.setId(group.getId() + "." + message.getName());
                group.getNodes().add(message);

                visit(n.getType(), message);
                n.getAnnotations().forEach(p -> visit(p, message));
                n.getParameters().forEach(p -> visit(p, message));
                n.getComment().ifPresent(l -> visit(l, message));
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
        ResolvedTypes astResolvedType = ResolvedTypes.of(type);
        if (astResolvedType.resolved) {
            message.getResponse().setBody(astResolvedType.getValue());
            message.getResponse().getCells().addAll(astResolvedType.cells);
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
        HttpRequest request = message.getRequest();
        Parameters parameters = Parameters.of(n);
        request.getCells().addAll(parameters.getCells());
        if (parameters.isFile()) {
            //File 修改请求头为 form data
            if (HttpRequestMethod.GET.equals(request.getMethod())) {
                request.setMethod(HttpRequestMethod.POST);
            }
            request.getHeaders().setContentType(HttpHeaders.ContentType.MULTIPART_FORM_DATA);
        }else if(parameters.isHeader()){
            request.getHeaders().put(parameters.getName(),String.valueOf(parameters.getValue()));
        }else if (parameters.isRequestBody()) {
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

    /**
     * 请求方法的注解处理
     *
     * @param n
     * @param message
     */
    private void visit(Comment n, HttpMessage message) {
        Comments comments = Comments.of(n);
        if (StringUtils.isNotEmpty(comments.name)) {
            message.setName(comments.name);
        }
        if (StringUtils.isNotEmpty(comments.description)) {
            message.setDescription(comments.description);
        }
    }

}
