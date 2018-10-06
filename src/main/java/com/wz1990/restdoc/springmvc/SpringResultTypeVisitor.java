package com.wz1990.restdoc.springmvc;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.wz1990.restdoc.ast.AstGeneric;
import com.wz1990.restdoc.core.RestDoc;
import com.wz1990.restdoc.helper.Entity;
import com.wz1990.restdoc.helper.JsonHelper;
import com.wz1990.restdoc.helper.ParsedJavadoc;
import com.wz1990.restdoc.schema.Item;

public class SpringResultTypeVisitor {

    public void visit(MethodDeclaration n, Item item, ParsedJavadoc parsedJavadoc, RestDoc restDoc){
        if(n.getType() instanceof ClassOrInterfaceType){
            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) n.getType();
            String name = classOrInterfaceType.getNameAsString();
            Entity entity = restDoc.getEntityHolder().getByName(name);
            ObjectNode resultNode = entity.getJsonNode().deepCopy();
            for (int i = 0; i < entity.getFields().size(); i++) {
                Entity.Field field = entity.getFields().get(i);
                if(entity.isGeneric(field) && classOrInterfaceType.getTypeArguments().isPresent()){
                    AstGeneric astGeneric = (AstGeneric) field.getValue();
                    NodeList<Type> types = classOrInterfaceType.getTypeArguments().get();
                    Type type = types.get(astGeneric.getIndex());
                    if(type instanceof ClassOrInterfaceType){
                        ClassOrInterfaceType generic = (ClassOrInterfaceType) type;
                        Entity genericEntity = restDoc.getEntityHolder().getByName(generic.getNameAsString());
                        resultNode.set(field.getName(),genericEntity.getJsonNode());
                    }

                }
            }
            Item.Response response = item.getResponse();
            response.getHeader().add(Item.Header.APPLICATION_JSON);
            response.setBody(JsonHelper.toPretty(resultNode));
        }
    }

}
