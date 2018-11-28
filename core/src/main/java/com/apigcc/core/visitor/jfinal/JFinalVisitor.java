package com.apigcc.core.visitor.jfinal;


import com.apigcc.core.handler.postman.schema.Method;
import com.apigcc.core.http.HttpMessage;
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
import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
import com.sun.webkit.network.URLs;

import java.rmi.server.RMIClientSocketFactory;
import java.util.ArrayList;
import java.util.List;


public class JFinalVisitor extends NodeVisitor {

    public static final String PACKAGE="com.jfinal";

    @Override
    public boolean accept(CompilationUnit cu) {
        return cu.getImports().stream().anyMatch(importDeclaration -> importDeclaration.getNameAsString().startsWith(PACKAGE));
    }

    /**
     *
     *
     * @param n
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Node arg) {
        /*如果是config*/
        if(arg instanceof Tree && n.getExtendedTypes().size()>0 && Configs.acceptConfig(n.getExtendedTypes(0))){
            System.out.println("config");
//            Tree tree = (Tree) arg;
//            Group group = new Group();
//            group.setId(Clazz.getFullName(n));
//            group.setName(Clazz.getNameInScope(n));
//
//            Bucket bucket = tree.getBucket(group.getBucketName());
//            group.setParent(bucket);
//            bucket.getGroups().add(group);
//
//            arg=group;

            analysisConfig(n.getMethods());
        }
        /*如果是controller 文件*/
        if(arg instanceof Tree && n.getExtendedTypes().size()>0  && Configs.acceptController(n.getExtendedTypes(0))){

            //解析controller
            analysisController(n.getMethods());
        }

        //所有的文件存放都放入
       // visit(n.getMethods(),n.getName(),arg);

        super.visit(n, arg);
    }


    public void analysisController(List<MethodDeclaration> methodDeclarations){
        System.out.println("解析controller");
        for (MethodDeclaration m:methodDeclarations) {
            System.out.println(m.getNameAsString());
        }
    }

    public void analysisConfig(List<MethodDeclaration> methodDeclarations){
        System.out.println("解析Config配置文件");
        for (MethodDeclaration m:methodDeclarations) {
            System.out.println(m.getNameAsString());
        }
    }

    /**
     * 代码块解析
     * @param stmt
     */
    public void visit(BlockStmt stmt){
        System.out.println("====");
        RC rc=RC.getInstance();
        List<RC> rcList=new ArrayList<>();
        rcList.add(new RC("/","IndexController.class"));
        rcList.add(new RC("/User","UserController.class"));
        rc.setRcs(rcList);

    }

    public void visit(List<MethodDeclaration> method,SimpleName name,Node arg){
        RC rc=RC.getInstance();
//        if(method.size()>0){
//            for (RC rc1:rc.getRcs()) {
//                if(rc1.getClazz().equals(name)){
//                    rc1.setPath(rc.getPath()+name);
//                }
//            }
//        }


        System.out.println(rc.getRcs());
    }

}
