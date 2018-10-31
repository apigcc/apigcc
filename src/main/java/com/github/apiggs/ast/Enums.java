package com.github.apiggs.ast;

import com.github.apiggs.schema.Appendix;
import com.github.apiggs.schema.Cell;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Enums {

    public static String getNames(ResolvedEnumDeclaration enumDeclaration){
        StringBuilder sb = new StringBuilder();
        for (ResolvedEnumConstantDeclaration resolvedEnumConstantDeclaration : enumDeclaration.getEnumConstants()) {
            if(sb.length()>0){
                sb.append(",");
            }
            sb.append(resolvedEnumConstantDeclaration.getName());
        }
        return sb.toString();
    }

    public static Appendix toDetails(EnumDeclaration declaration){
        Appendix appendix = new Appendix();
        appendix.setName(declaration.getNameAsString());

        Comments.of(declaration.getComment()).ifPresent(comments -> {
            if(!Strings.isNullOrEmpty(comments.content)){
                appendix.setName(comments.content);
            }
        });

        for (EnumConstantDeclaration constant : declaration.getEntries()) {
            Cell cell = new Cell();
            cell.setName(constant.getNameAsString());
            for (Expression expression : constant.getArguments()) {
                Object value = Expressions.getValue(expression);
                StringBuilder stringBuilder = new StringBuilder();
                if(cell.getValue()==null){
                    cell.setValue(value);
                }else{
                    stringBuilder.append(value).append(" ");
                }
                cell.setDescription(stringBuilder.toString());
            }
            appendix.getCells().add(cell);
        }
        return appendix;
    }

}
