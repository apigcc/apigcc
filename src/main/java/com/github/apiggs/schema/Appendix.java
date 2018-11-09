package com.github.apiggs.schema;

import com.github.apiggs.common.Cell;
import com.github.apiggs.resolver.ast.Enums;
import com.github.apiggs.resolver.ast.Fields;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 附录
 */
@Setter
@Getter
public class Appendix extends Node {

    List<Cell<String>> cells = new ArrayList<>();

    public boolean isEmpty() {
        return cells.isEmpty();
    }

    @Nullable
    public static Appendix parse(JavadocComment n) {
        if (!n.getCommentedNode().isPresent()) {
            return null;
        }
        final com.github.javaparser.ast.Node node = n.getCommentedNode().get();
        if(!(node instanceof BodyDeclaration)){
            return null;
        }
        final BodyDeclaration bodyDeclaration = (BodyDeclaration) node;
        if(!bodyDeclaration.isEnumDeclaration() && !bodyDeclaration.isClassOrInterfaceDeclaration()){
            return null;
        }
        Appendix appendix = new Appendix();
        if (bodyDeclaration.isEnumDeclaration()) {
            appendix.getCells().addAll(Enums.toDetails(bodyDeclaration.asEnumDeclaration()));
        }else if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            appendix.getCells().addAll(Fields.getConstants(bodyDeclaration.asClassOrInterfaceDeclaration()));
        }
        if (node instanceof NodeWithSimpleName) {
            appendix.setName(((NodeWithSimpleName) node).getNameAsString());
        }
        appendix.accept(node.getComment());
        return appendix;
    }

}
