package com.apigcc.core.resolver.ast;

import com.apigcc.core.common.Cell;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Cell<String>> toDetails(EnumDeclaration declaration){
        List<Cell<String>> cells = new ArrayList<>();
        for (EnumConstantDeclaration constant : declaration.getEntries()) {
            Cell<String> cell = new Cell<>();
            cell.add(constant.getNameAsString());
            for (Expression expression : constant.getArguments()) {
                Object value = Expressions.getValue(expression);
                cell.add(String.valueOf(value));
            }
            cells.add(cell);
        }
        return cells;
    }

}
