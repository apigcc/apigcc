package com.apigcc.http;

import com.apigcc.resolver.ResolvedTypes;
import com.apigcc.resolver.ast.Comments;
import com.apigcc.resolver.ast.Tag;
import com.apigcc.schema.Group;
import com.apigcc.schema.Node;
import com.apigcc.Context;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.google.common.base.Strings;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import lombok.Setter;

/**
 * An class that defines a HTTP message,
 * providing common properties and method
 */
@Getter
@Setter
public class HttpMessage extends Node {

    HttpVersion version = HttpVersion.DEFAULT;
    HttpRequest request = new HttpRequest();
    HttpResponse response = new HttpResponse();

    @Ignore
    Group parent;

    @Override
    public void accept(Comments comments) {
        super.accept(comments);

        //解析@return标签
        for (Tag tag : comments.getTags()) {
            if ("return".equals(tag.getName()) && !Strings.isNullOrEmpty(tag.getContent())) {
                SymbolReference<ResolvedReferenceTypeDeclaration> symbolReference = Context.getContext().getTypeSolver().tryToSolveType(tag.getContent());
                if (symbolReference.isSolved()) {
                    ResolvedReferenceTypeDeclaration typeDeclaration = symbolReference.getCorrespondingDeclaration();
                    ResolvedTypes resolvedTypes = ResolvedTypes.of(typeDeclaration);
                    if (resolvedTypes.resolved) {
                        response.setBody(resolvedTypes.getValue());
                        response.getCells().addAll(resolvedTypes.cells);
                    }
                }

            }
        }

    }

}
