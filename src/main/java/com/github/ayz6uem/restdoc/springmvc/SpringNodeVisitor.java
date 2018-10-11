package com.github.ayz6uem.restdoc.springmvc;

import com.github.ayz6uem.restdoc.NodeVisitor;
import com.github.ayz6uem.restdoc.http.HttpHeaders;
import com.github.ayz6uem.restdoc.http.HttpMessage;
import com.github.ayz6uem.restdoc.http.HttpRequest;
import com.github.ayz6uem.restdoc.http.HttpRequestMethod;
import com.github.ayz6uem.restdoc.schema.Cell;
import com.github.ayz6uem.restdoc.schema.Group;
import com.github.ayz6uem.restdoc.schema.Node;
import com.github.ayz6uem.restdoc.schema.Tree;
import com.github.ayz6uem.restdoc.ast.Comments;
import com.github.ayz6uem.restdoc.util.URL;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.Type;
import com.github.ayz6uem.restdoc.ast.ASTResolvedType;

import java.util.Optional;

/**
 * Spring endpoints解析
 */
public class SpringNodeVisitor extends NodeVisitor {

    /**
     * 查找Endpoints接入类
     * @param n
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Node arg) {
        if(arg != null && arg instanceof Tree){
            Tree tree = (Tree) arg;
            if (Controllers.accept(n.getAnnotations())) {
                String name = ASTResolvedType.getNameInScope(n);
                String fullName = ASTResolvedType.getFullName(n);
                Group group = new Group();
                group.setParent(tree);
                group.setId(fullName);
                group.setName(name);
                //path 和 method 影响方法的处理
                Optional<RequestMappings> optional = RequestMappings.of(n);
                if(optional.isPresent()){
                    group.getExt().put("path", optional.get().getPath());
                    group.getExt().put("method", optional.get().getMethod());
                }
                tree.getNodes().add(group);

                super.visit(n, group);
            }
        }
        super.visit(n, arg);
    }

    /**
     * 请求方法处理
     * @param n
     * @param arg
     */
    @Override
    public void visit(MethodDeclaration n, Node arg) {
        if (arg != null && arg instanceof Group && RequestMappings.accept(n.getAnnotations())) {
            Group group = (Group) arg;
            //请求方法处理成HttpMessage
            HttpMessage message = new HttpMessage();
            message.setParent(group);
            message.setName(n.getNameAsString());
            message.setId(group.getId() + "." + message.getName());
            group.getNodes().add(message);

            visit(n.getType(), message);
            n.getParameters().forEach(p -> visit(p, message));
            //TODO 没有body，有cells时，生成queryString。
            n.getAnnotations().forEach(p -> visit(p, message));
            n.getComment().ifPresent(l -> visit(l, message));

        }
        super.visit(n, arg);
    }

    /**
     * 解析方法返回值
     * @param type
     * @param message
     */
    private void visit(Type type, HttpMessage message){
        ASTResolvedType astResolvedType = ASTResolvedType.of(type);
        if(astResolvedType!=null){
            message.getResponse().setBody(astResolvedType.getValue());
            message.getResponse().getCells().addAll(astResolvedType.getCells());
        }
    }

    /**
     * 解析方法参数
     * 有@RequestBody时，请求方法由GET改为POST，
     * @param n
     * @param message
     */
    private void visit(Parameter n, HttpMessage message) {
        HttpRequest request = message.getRequest();
        Parameters parameters = Parameters.of(n);
        request.getCells().addAll(parameters.getCells());
        if(parameters.isFile()){
            //File 修改请求头为 form data
            if(HttpRequestMethod.GET.equals(request.getMethod())){
                request.setMethod(HttpRequestMethod.POST);
            }
            request.getHeaders().setContentType(HttpHeaders.ContentType.MULTIPART_FORM_DATA);
        }
        if(parameters.isRequestBody()){
            //RequestBody 修改请求头为json
            if(HttpRequestMethod.GET.equals(request.getMethod())){
                request.setMethod(HttpRequestMethod.POST);
            }
            request.getHeaders().setContentType(HttpHeaders.ContentType.APPLICATION_JSON);
            request.setBody(parameters.getValue());
        }
    }

    /**
     * 请求方法的注解处理
     * @param n
     * @param message
     */
    private void visit(AnnotationExpr n, HttpMessage message) {
        if (!RequestMappings.accept(n)) {
            return;
        }
        Group group = (Group) message.getParent();

        RequestMappings requestMappings = RequestMappings.of(n);
        message.getRequest().setMethod(requestMappings.getMethod());
        //根据method 设置默认content-Type
        message.getRequest().getHeaders().setContentType(requestMappings.getMethod());
        message.getRequest().setUri(URL.normalize(group.getExt().get("path"), requestMappings.getPath()));
        message.getRequest().getHeaders().add(requestMappings.getHeaders());
    }

    /**
     * 请求方法的注解处理
     * @param n
     * @param message
     */
    private void visit(Comment n, HttpMessage message) {
        Comments comments = Comments.of(n);
        message.setName(comments.getName());
        message.setDescription(comments.getDescription());
        for (int i = 0; i < message.getRequest().getCells().size(); i++) {
            Cell cell = message.getRequest().getCells().get(i);
            String description = comments.getParams().get(cell.getName());
            cell.setDescription(description);
        }

    }

}
