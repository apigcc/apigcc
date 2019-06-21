package com.apigcc.spring;

import com.apigcc.common.*;
import com.apigcc.parser.ParserStrategy;
import com.apigcc.schema.Chapter;
import com.apigcc.schema.Section;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;

public class SpringParserStrategy implements ParserStrategy {

    public static final String ANNOTATION_CONTROLLER = "Controller";
    public static final String ANNOTATION_REST_CONTROLLER = "RestController";

    public static final String EXT_URI = "uri";

    @Override
    public boolean accept(ClassOrInterfaceDeclaration n) {
        return n.isAnnotationPresent(ANNOTATION_REST_CONTROLLER);
    }

    @Override
    public boolean accept(MethodDeclaration n) {
        return AnnotationHelper.isAnnotationPresent(n, RequestMappingHelper.ANNOTATION_REQUEST_MAPPINGS);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Chapter chapter) {
        chapter.getExt().put(EXT_URI, RequestMappingHelper.pickUriToParent(n));
    }

    @Override
    public void visit(MethodDeclaration n, Chapter chapter, Section section) {
        visitMethod(n, chapter, section);
        visitUri(n, chapter, section);
        visitParameters(n, chapter, section);
        visitReturn(n, chapter, section);
    }

    private void visitMethod(MethodDeclaration n, Chapter chapter, Section section){
        section.setMethod(RequestMappingHelper.pickMethod(n));
    }

    private void visitUri(MethodDeclaration n, Chapter chapter, Section section) {
        URI uri = (URI) chapter.getExt().get(EXT_URI);
        section.setUri(new URI(uri.toString()).add(RequestMappingHelper.pickUri(n.getAnnotations())).toString());
    }

    private void visitParameters(MethodDeclaration n, Chapter chapter, Section section) {

        for (Parameter parameter : n.getParameters()) {
            if (ParameterHelper.isPathVariable(parameter)) {
                section.getPathVariable().put(parameter.getNameAsString(), "");
            }
        }

        if (ParameterHelper.hasRequestBody(n.getParameters())) {
            section.setQueryParameter(false);
            Parameter parameter = ParameterHelper.getRequestBody(n.getParameters());
            TypeDescription description = ResolvedTypes.resolve(parameter.getType());
            if(description.isArray()){
                section.setParameter(description.getArrayNode());
            }else if(description.isBean()){
                section.setParameter(description.getObjectNode());
            }
            section.addRequestRows(description.getRows());

        } else {
            ObjectNode objectNode = ObjectMappers.instance.createObjectNode();
            for (Parameter parameter : n.getParameters()) {
                if (ParameterHelper.isRequestParam(parameter)) {
                    Type type = parameter.getType();
                    String key = parameter.getNameAsString();
                    TypeDescription description = ResolvedTypes.resolve(type);
                    if (description.isPrimitive()) {
                        TypeDescription.setNumber(objectNode, key, description.getPrimitive());
                        description.fieldName(key);
                    }else if(description.isString()){
                        objectNode.put(key, description.getCharSequence().toString());
                        description.fieldName(key);
                    }else if(description.isArray()){
                        objectNode.set(key+"[]", description.getArrayNode());
                        description.fieldName(key+"[]");
                    }else if(description.isBean()){
                        ObjectMappers.merge(objectNode, description.getObjectNode());
                    }
                    section.addRequestRows(description.getRows());
                }
            }
            section.setParameter(objectNode);
        }
    }

    private void visitReturn(MethodDeclaration n, Chapter chapter, Section section) {
        TypeDescription description = ResolvedTypes.resolve(n.getType());
        if(description.isArray()){
            section.setResponse(description.getArrayNode());
        }else if(description.isBean()){
            section.setResponse(description.getObjectNode());
        }
        section.addResponseRows(description.getRows());
    }

}
