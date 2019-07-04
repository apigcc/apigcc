package com.apigcc.spring;

import com.apigcc.common.ResolvedTypes;
import com.apigcc.common.URI;
import com.apigcc.common.description.ObjectTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.helper.AnnotationHelper;
import com.apigcc.parser.ParserStrategy;
import com.apigcc.schema.Chapter;
import com.apigcc.schema.Row;
import com.apigcc.schema.Section;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.google.common.collect.Lists;

import java.util.List;

public class SpringParserStrategy implements ParserStrategy {

    public static final String ANNOTATION_CONTROLLER = "Controller";
    public static final String ANNOTATION_REST_CONTROLLER = "RestController";

    public static final String EXT_URI = "uri";

    public static final List<String> ANNOTATION_CONTROLLERS = Lists.newArrayList(ANNOTATION_CONTROLLER, ANNOTATION_REST_CONTROLLER);

    /**
     * TODO
     * 处理被@RestController和@Controller标记的类
     * @param n
     * @return
     */
    @Override
    public boolean accept(ClassOrInterfaceDeclaration n) {
        return AnnotationHelper.isAnnotationPresent(n,ANNOTATION_CONTROLLERS);
    }

    /**
     * TODO
     * 类被@RestController标记，或方法被@ResponseBody标记
     * @param n
     * @return
     */
    @Override
    public boolean accept(MethodDeclaration n) {
        return AnnotationHelper.isAnnotationPresent(n, RequestMappingHelper.ANNOTATION_REQUEST_MAPPINGS);
    }

    /**
     * 解析类定义
     * @param n
     * @param chapter
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Chapter chapter) {
        chapter.getExt().put(EXT_URI, RequestMappingHelper.pickUriToParent(n));
    }

    /**
     * 解析方法定义
     * @param n
     * @param chapter
     * @param section
     */
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
                Row row = new Row();
                row.setKey(parameter.getNameAsString());
                row.setType(parameter.getType().toString());
                section.getParamTag(row.getKey()).ifPresent(tag-> row.setRemark(tag.getContent()));
                section.addRequestRow(row);
            }
        }

        if (ParameterHelper.hasRequestBody(n.getParameters())) {
            section.setQueryParameter(false);
            Parameter parameter = ParameterHelper.getRequestBody(n.getParameters());
            TypeDescription description = ResolvedTypes.resolve(parameter.getType());
            if(description.isArray()){
                section.setParameter(description.asArray().getValue());
            }else if(description.isObject()){
                section.setParameter(description.asObject().getValue());
            }
            section.addRequestRows(description.rows());

        } else {
            ObjectTypeDescription objectTypeDescription = new ObjectTypeDescription();
            for (Parameter parameter : n.getParameters()) {
                if (ParameterHelper.isRequestParam(parameter)) {
                    String key = parameter.getNameAsString();
                    TypeDescription description = ResolvedTypes.resolve(parameter.getType());
                    description.setKey(key);
                    section.getParamTag(key).ifPresent(tag->description.setRemark(tag.getContent()));
                    if(description.isObject()){
                        objectTypeDescription.merge(description.asObject());
                    }else{
                        objectTypeDescription.add(description);
                    }
                }
            }
            section.setParameter(objectTypeDescription.getValue());
            section.addRequestRows(objectTypeDescription.rows());
        }
    }

    private void visitReturn(MethodDeclaration n, Chapter chapter, Section section) {
        TypeDescription description = ResolvedTypes.resolve(n.getType());
        if(description.isArray()){
            section.setResponse(description.asArray().getValue());
        }else if(description.isObject()){
            section.setResponse(description.asObject().getValue());
        }
        section.addResponseRows(description.rows());
    }

}
