package com.github.apiggs.ast;

import com.github.apiggs.schema.Cell;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;

import java.util.ArrayList;
import java.util.List;
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

    public static List<Cell> toDetails(EnumDeclaration declaration){
        List<Cell> list = new ArrayList<>();
        list.addAll(declaration.asEnumDeclaration().getEntries().stream().map(constant -> {
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
            return cell;
        }).collect(Collectors.toList()));
        return list;
    }

}
