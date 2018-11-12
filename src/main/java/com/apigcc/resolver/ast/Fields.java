package com.apigcc.resolver.ast;

import com.apigcc.common.Cell;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fields {

    public static final String ANNOTATION_ALIAS_JACKSON = "JsonProperty";
    public static final String ANNOTATION_ALIAS_GSON = "SerializedName";
    public static final String ANNOTATION_ALIAS_FASTJSON = "JSONField";

    public static Object getInitializer(JavaParserFieldDeclaration field){
        Optional<Expression> initializer = field.getVariableDeclarator().getInitializer();
        if(initializer.isPresent()){
            Expression expr = initializer.get();
            return Expressions.getValue(expr);
        }
        return null;
    }


    public static String getName(ResolvedFieldDeclaration n) {
        if(n instanceof JavaParserFieldDeclaration){
            JavaParserFieldDeclaration field = (JavaParserFieldDeclaration)n;
            FieldDeclaration declaration = field.getWrappedNode();
            if(declaration.getAnnotationByName(ANNOTATION_ALIAS_JACKSON).isPresent()){
                Object value = Annotations.getAttr(declaration.getAnnotationByName(ANNOTATION_ALIAS_JACKSON),"value");
                if(value!=null){
                    return String.valueOf(value);
                }
            }
            if(declaration.getAnnotationByName(ANNOTATION_ALIAS_GSON).isPresent()){
                Object value = Annotations.getAttr(declaration.getAnnotationByName(ANNOTATION_ALIAS_GSON),"value");
                if(value!=null){
                    return String.valueOf(value);
                }
            }
            if(declaration.getAnnotationByName(ANNOTATION_ALIAS_FASTJSON).isPresent()){
                Object value = Annotations.getAttr(declaration.getAnnotationByName(ANNOTATION_ALIAS_FASTJSON),"name");
                if(value!=null){
                    return String.valueOf(value);
                }
            }

        }
        return n.getName();
    }

    public static List<Cell<String>> getConstants(ClassOrInterfaceDeclaration declaration) {
        List<Cell<String>> cells = new ArrayList<>();
        for (FieldDeclaration field : declaration.getFields()) {
            if(field.isStatic() && field.isPublic() && field.isFinal()){
                VariableDeclarator variable = field.getVariable(0);
                String value = null;
                String description = null;
                if(variable.getInitializer().isPresent()){
                    value = String.valueOf(Expressions.getValue(variable.getInitializer().get()));
                }
                Optional<Comments> optional = Comments.of(field.getComment());
                if(optional.isPresent()){
                    description = optional.get().content;
                }
                cells.add(new Cell<>(variable.getNameAsString(),value,description));
            }
        }

        return cells;
    }
}
