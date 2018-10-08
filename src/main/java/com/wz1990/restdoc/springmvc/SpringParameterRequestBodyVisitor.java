package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.body.Parameter;
import com.wz1990.restdoc.RestDoc;
import com.wz1990.restdoc.ast.AstHelper;
import com.wz1990.restdoc.ast.AstType;
import com.wz1990.restdoc.helper.Entity;
import com.wz1990.restdoc.helper.JsonHelper;
import com.wz1990.restdoc.schema.BodyMode;
import com.wz1990.restdoc.schema.Item;

import java.util.Objects;

public class SpringParameterRequestBodyVisitor {


    public void visit(Item item, Parameter p, RestDoc restDoc) {
        Item.Request request = item.getRequest();

        AstType astType = AstHelper.parseType(p.getType());

        request.getBody().setMode(BodyMode.raw);
        request.getHeader().add(Item.Header.APPLICATION_JSON);

        if (AstHelper.isBaseType(astType.getCompoent())) {
            request.getBody().setRaw(JsonHelper.toArrayPretty(AstHelper.defaultValue(astType.getCompoent())));
        } else {
            Entity entity = restDoc.getEntityHolder().getByName(astType.getCompoent());
            if (Objects.nonNull(entity)) {
                request.getBody().setEntity(entity);
                if (astType.isArray()) {
                    request.getBody().setRaw(JsonHelper.toArrayPretty(entity.getJsonNode()));
                } else {
                    request.getBody().setRaw(entity.getPrettyJson());
                }
                addRawParameter(request, entity);
            }
        }
    }

    private void addRawParameter(Item.Request request, Entity entity) {
        for (int i = 0; i < entity.getFields().size(); i++) {
            Entity.Field field = entity.getFields().get(i);

            Item.Parameter parameter = new Item.Parameter();
            parameter.setKey(field.getName());
            parameter.setType(field.getType());
            parameter.setValue(AstHelper.defaultValue(field.getType()));
            parameter.setDescription(field.getDescription());
            request.getBody().getRawParameter().add(parameter);
        }
    }

}
