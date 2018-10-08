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
                addPathVariable(item.getRequest(), p.getNameAsString(), p.getTypeAsString(), description);
            }
        }
    }
    private void addPathVariable(Item.Request request, String key, String type, String description) {
        Item.Parameter parameter = new Item.Parameter();
        parameter.setKey(key);
        parameter.setType(type);
        parameter.setValue(AstHelper.defaultValue(type));
        parameter.setDescription(description);
        request.getUrl().getPathVariable().add(parameter);
    }

}
