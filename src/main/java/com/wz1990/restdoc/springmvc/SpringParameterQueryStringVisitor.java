package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.wz1990.restdoc.RestDoc;
import com.wz1990.restdoc.ast.AstHelper;
import com.wz1990.restdoc.helper.Entity;
import com.wz1990.restdoc.helper.ParsedJavadoc;
import com.wz1990.restdoc.schema.Item;

import java.util.Objects;

public class SpringParameterQueryStringVisitor {

    public void visit(Item item, Parameter p, ParsedJavadoc parsedJavadoc, RestDoc restDoc) {
        if (p.getType() instanceof ClassOrInterfaceType) {
            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) p.getType();
            if (AstHelper.isBaseType(classOrInterfaceType)) {
                String description = parsedJavadoc.getParams().get(p.getNameAsString());
                addQueryParameter(item.getRequest(), p.getNameAsString(), p.getTypeAsString(), description);
            } else {
                Entity entity = restDoc.getEntityHolder().getByName(classOrInterfaceType.getNameAsString());
                if (Objects.nonNull(entity)) {
                    for (int i = 0; i < entity.getFields().size(); i++) {
                        Entity.Field field = entity.getFields().get(i);
                        addQueryParameter(item.getRequest(), field.getName(), field.getType(), field.getDescription());
                    }
                }
            }
        }
    }

    private void addQueryParameter(Item.Request request, String key, String type, String description) {
        Item.Parameter parameter = new Item.Parameter();
        parameter.setKey(key);
        parameter.setType(type);
        parameter.setValue(AstHelper.defaultValue(type));
        parameter.setDescription(description);
        request.getUrl().getQuery().add(parameter);
    }

}
