package com.apigcc.core.visitor.jfinal;

import com.apigcc.core.visitor.NodeVisitor;
import com.github.javaparser.ast.CompilationUnit;

public class JFinalVisitor extends NodeVisitor {

    @Override
    public boolean accept(CompilationUnit cu) {
        return false;
    }

}
