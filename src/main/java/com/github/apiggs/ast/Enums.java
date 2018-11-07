package com.github.apiggs.ast;

import com.github.apiggs.schema.Appendix;
import com.github.apiggs.util.Cell;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.google.common.base.Strings;

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

        appendix.accept(declaration.getComment());

        for (EnumConstantDeclaration constant : declaration.getEntries()) {
            Cell<String> cell = new Cell<>();
            cell.add(constant.getNameAsString());
            for (Expression expression : constant.getArguments()) {
                Object value = Expressions.getValue(expression);
                cell.add(String.valueOf(value));
            }
            appendix.getCells().add(cell);
        }
        return appendix;
    }

}
