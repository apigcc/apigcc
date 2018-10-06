package com.wz1990.restdoc.helper;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.wz1990.restdoc.ast.AstGeneric;
import com.wz1990.restdoc.ast.AstHelper;

import java.util.EnumSet;

public class EntityVisitor extends VoidVisitorAdapter<EntityHolder> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, EntityHolder entityHolder) {

        Entity entity = new Entity();
        entity.setName(n.getNameAsString());
        entity.setFullName(AstHelper.getFullName(n));

        if(n.getExtendedTypes().isNonEmpty()){
            ClassOrInterfaceType parentType = n.getExtendedTypes().get(0);
            entity.setParentName(parentType.getNameAsString());
        }

        if(n.getTypeParameters().isNonEmpty()){
            for (int i = 0; i < n.getTypeParameters().size(); i++) {
                TypeParameter typeParameter = n.getTypeParameter(i);
                entity.addGeneric(typeParameter,i);
            }
        }

        for (int i = 0; i < n.getMembers().size(); i++) {
            BodyDeclaration bodyDeclaration = n.getMembers().get(i);
            if (bodyDeclaration instanceof FieldDeclaration) {
                FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
                parseField(entity, fieldDeclaration);
            }
        }

        if (entity.getFields().size() > 0) {
            entityHolder.put(entity.getName(), entity.getFullName(), entity);
        }

    }

    private void parseField(Entity entity, FieldDeclaration fieldDeclaration) {
        EnumSet<Modifier> modifiers = fieldDeclaration.getModifiers();
        if (modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.FINAL) || modifiers.contains(Modifier.ABSTRACT)) {
            return;
        }
        String content = JavadocHelper.getContent(fieldDeclaration.getComment());
        for (int i = 0; i < fieldDeclaration.getVariables().size(); i++) {
            VariableDeclarator variableDeclarator = fieldDeclaration.getVariables().get(i);
            Entity.Field field = new Entity.Field();

            field.setType(variableDeclarator.getTypeAsString());

            if(entity.isGeneric(variableDeclarator.getTypeAsString())){
                field.setValue(entity.getGeneric(variableDeclarator.getTypeAsString()));
            }else{
                field.setValue(AstHelper.defaultValue(field.getType()));
            }
            field.setName(variableDeclarator.getNameAsString());
            field.setDescription(content);
            entity.getFields().add(field);
        }
    }

}
