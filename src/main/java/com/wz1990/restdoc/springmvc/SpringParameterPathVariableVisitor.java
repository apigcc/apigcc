package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.wz1990.restdoc.ast.AstHelper;
import com.wz1990.restdoc.helper.ParsedJavadoc;
import com.wz1990.restdoc.schema.Item;

public class SpringParameterPathVariableVisitor {

    public void visit(Item item, Parameter p, ParsedJavadoc parsedJavadoc) {
        if (p.getType() instanceof ClassOrInterfaceType) {
            ClassOrInterfaceType type = (ClassOrInterfaceType) p.getType();
            if (AstHelper.isBaseType(type)) {
                String description = parsedJavadoc.getParams().get(p.getNameAsString());
                item.getRequest().addDescription(description(p.getTypeAsString(),p.getNameAsString(),description));
            }
        }
    }

    public String description(String type, String name, String description) {
        return String.join(" ", name, type, description);
    }

}
