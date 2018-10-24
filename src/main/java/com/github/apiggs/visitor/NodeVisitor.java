package com.github.apiggs.visitor;

import com.github.apiggs.Apiggs;
import com.github.apiggs.schema.Node;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public abstract class NodeVisitor extends VoidVisitorAdapter<Node> {

    protected Apiggs context;

    public void setContext(Apiggs context) {
        this.context = context;
    }
}
