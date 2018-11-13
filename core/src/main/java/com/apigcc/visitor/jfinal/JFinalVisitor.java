package com.apigcc.visitor.jfinal;

import com.apigcc.visitor.NodeVisitor;
import com.github.javaparser.ast.CompilationUnit;

public class JFinalVisitor extends NodeVisitor {

    @Override
    public boolean accept(CompilationUnit cu) {
        return false;
    }

}
