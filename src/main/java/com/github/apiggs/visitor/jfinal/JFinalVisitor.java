package com.github.apiggs.visitor.jfinal;

import com.github.apiggs.visitor.NodeVisitor;
import com.github.javaparser.ast.CompilationUnit;

public class JFinalVisitor extends NodeVisitor {

    @Override
    public boolean accept(CompilationUnit cu) {
        return false;
    }

}
