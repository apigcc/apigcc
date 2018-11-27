package com.apigcc.core.visitor.jfinal;


import com.apigcc.core.handler.postman.schema.Method;
import com.apigcc.core.resolver.ast.Clazz;
import com.apigcc.core.schema.Bucket;
import com.apigcc.core.schema.Group;
import com.apigcc.core.schema.Node;
import com.apigcc.core.schema.Tree;
import com.apigcc.core.visitor.NodeVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.util.List;


public class JFinalVisitor extends NodeVisitor {

    public static final String PACKAGE="com.jfinal";

    @Override
    public boolean accept(CompilationUnit cu) {
        return cu.getImports().stream().anyMatch(importDeclaration -> importDeclaration.getNameAsString().startsWith(PACKAGE));
    }

    /**
     * 查找Endpoints接入类
     *
     * @param n
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Node arg) {
        if(arg instanceof Tree && Configs.accept(n.getExtendedTypes())){
            Tree tree = (Tree) arg;
            Group group = new Group();
            group.setId(Clazz.getFullName(n));
            group.setName(Clazz.getNameInScope(n));

            Bucket bucket = tree.getBucket(group.getBucketName());
            group.setParent(bucket);
            bucket.getGroups().add(group);

            arg=group;
        }

        if(arg instanceof Tree && Configs.accept(n.getName())){
            visit(n.getMethods(),n.getName());
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
        if (arg instanceof Group && Configs.accept(n.getName())) {
            visit(n.getBody().get().asBlockStmt());
        }
    }

    /**
     * 代码块解析
     * @param stmt
     */
    public void visit(BlockStmt stmt){
        List<RC> rcs=RC.getInstance();
        rcs.add(new RC("/","IndexController.class"));
        rcs.add(new RC("/User","UserController.class"));
    }

    public void visit(List<MethodDeclaration> method,SimpleName name){
        if(method.size()>0){
            for (RC rc:RC.getInstance()) {
                if(rc.getClazz().equals(name)){
                    rc.setMethod(rc.getMethod());
                }
            }
        }

        System.out.println();
    }

}
