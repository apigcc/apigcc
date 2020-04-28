package com.github.apigcc.core.common.helper;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import java.util.Optional;

public class CompilationUnitHelper {

    public static Optional<CompilationUnit> getCompilationUnit(Node node){
        if(node instanceof CompilationUnit){
            return Optional.of((CompilationUnit) node);
        }
        if (node.getParentNode().isPresent()){
            return getCompilationUnit(node.getParentNode().get());
        }
        return Optional.empty();
    }

}
