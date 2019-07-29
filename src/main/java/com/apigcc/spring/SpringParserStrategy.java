package com.apigcc.spring;

import com.apigcc.common.helper.ExpressionHelper;
import com.apigcc.common.helper.StringHelper;
import com.apigcc.common.resolver.TypeResolvers;
import com.apigcc.common.URI;
import com.apigcc.common.description.ObjectTypeDescription;
import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.helper.AnnotationHelper;
import com.apigcc.parser.ParserStrategy;
import com.apigcc.schema.Chapter;
import com.apigcc.schema.Row;
import com.apigcc.schema.Section;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apigcc.spring.ParameterHelper.ANNOTATION_REQUEST_HEADER;
import static com.apigcc.spring.ParameterHelper.ANNOTATION_REQUEST_PARAM;

public class SpringParserStrategy implements ParserStrategy {

    public static final String ANNOTATION_CONTROLLER = "Controller";
    public static final String ANNOTATION_REST_CONTROLLER = "RestController";

    public static final String EXT_URI = "uri";

    public static final List<String> ANNOTATION_CONTROLLERS = Lists.newArrayList(ANNOTATION_CONTROLLER, ANNOTATION_REST_CONTROLLER);

    /**
     * 处理被@RestController和@Controller标记的类
     * @param n
     * @return
     */
    @Override
    public boolean accept(ClassOrInterfaceDeclaration n) {
        return AnnotationHelper.isAnnotationPresent(n,ANNOTATION_CONTROLLERS);
    }

    /**
     * 类被@RestController标记，或方法被@ResponseBody标记
     * @param n
     * @return
     */
    @Override
    public boolean accept(MethodDeclaration n) {
        return RequestMappingHelper.isRest(n) && AnnotationHelper.isAnnotationPresent(n, RequestMappingHelper.ANNOTATION_REQUEST_MAPPINGS);
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
        visitPathVariable(n, chapter, section);
        visitHeaders(n, chapter, section);
        visitParameters(n, chapter, section);
        visitReturn(n, chapter, section);
    }

    /**
     * 解析请求方法
     * @param n
     * @param chapter
     * @param section
     */
    private void visitMethod(MethodDeclaration n, Chapter chapter, Section section){
        section.setMethod(RequestMappingHelper.pickMethod(n));
    }

    /**
     * 解析请求URI，与父类URI拼接
     * @param n
     * @param chapter
     * @param section
     */
    private void visitUri(MethodDeclaration n, Chapter chapter, Section section) {
        URI uri = (URI) chapter.getExt().get(EXT_URI);
        section.setUri(new URI(uri.toString()).add(RequestMappingHelper.pickUri(n.getAnnotations())).toString());
    }

    /**
     * 解析方法参数
     * @param n
     * @param chapter
     * @param section
     */
    private void visitParameters(MethodDeclaration n, Chapter chapter, Section section) {
        if (ParameterHelper.hasRequestBody(n.getParameters())) {
            visitRequestBody(n, chapter, section);
        }else{
            visitParameter(n, chapter, section);
        }
    }

    /**
     * 解析PathVariable
     * @param n
     * @param chapter
     * @param section
     */
    private void visitPathVariable(MethodDeclaration n, Chapter chapter, Section section) {
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
    }

    /**
     * 解析RequestHeader
     * @param n
     * @param chapter
     * @param section
     */
    private void visitHeaders(MethodDeclaration n, Chapter chapter, Section section) {

        List<String> headers = RequestMappingHelper.pickHeaders(n.getAnnotations());
        section.getInHeaders().addAll(headers);

        List<String> consumers = RequestMappingHelper.pickConsumers(n.getAnnotations());
        if (!consumers.isEmpty()) {
            section.getInHeaders().add("Content-Type: "+ String.join(",", consumers));
        }

        List<String> produces = RequestMappingHelper.pickProduces(n.getAnnotations());
        if (!produces.isEmpty()) {
            section.getOutHeaders().add("Content-Type: "+ String.join(",", produces));
        }

        for (Parameter parameter : n.getParameters()) {
            if (ParameterHelper.isRequestHeader(parameter)) {
                String key = parameter.getNameAsString();
                String defaultValue = "{value}";
                AnnotationExpr annotationExpr = parameter.getAnnotationByName(ANNOTATION_REQUEST_HEADER).get();
                Optional<Expression> valueOptional = AnnotationHelper.getAnyAttribute(annotationExpr, "value", "name");
                if(valueOptional.isPresent()){
                    key = String.valueOf(ExpressionHelper.getValue(valueOptional.get()));
                }
                Optional<Expression> defaultValueOptional = AnnotationHelper.getAttribute(annotationExpr, "defaultValue");
                if(defaultValueOptional.isPresent()){
                    defaultValue = String.valueOf(ExpressionHelper.getValue(defaultValueOptional.get()));
                }
                TypeDescription description = TypeResolvers.resolve(parameter.getType());
                if(description.isAvailable()){
                    Object value = description.getValue();
                    if(StringHelper.isBlank(defaultValue) && StringHelper.nonBlank(value)){
                        defaultValue = String.valueOf(value);
                    }
                    section.getInHeaders().add(key + ": " + defaultValue);
                }
            }
        }
    }

    /**
     * 解析RequestBody
     * @param n
     * @param chapter
     * @param section
     */
    private void visitRequestBody(MethodDeclaration n, Chapter chapter, Section section) {
        section.setQueryParameter(false);
        for (Parameter parameter : n.getParameters()) {
            if (ParameterHelper.isRequestBody(parameter)) {
                TypeDescription description = TypeResolvers.resolve(parameter.getType());
                if (description.isAvailable()) {
                    if (description.isArray()) {
                        section.setParameter(description.asArray().getValue());
                    } else if (description.isObject()) {
                        section.setParameter(description.asObject().getValue());
                    }
                    section.addRequestRows(description.rows());
                }
                break;
            }
        }

    }

    /**
     * 解析RequestParameter
     * @param n
     * @param chapter
     * @param section
     */
    private void visitParameter(MethodDeclaration n, Chapter chapter, Section section) {
        ObjectTypeDescription objectTypeDescription = new ObjectTypeDescription();
        for (Parameter parameter : n.getParameters()) {
            if (ParameterHelper.isRequestParam(parameter)) {
                String key = parameter.getNameAsString();

                Object defaultValue = null;
                Boolean required = null;

                Optional<AnnotationExpr> optional = parameter.getAnnotationByName(ANNOTATION_REQUEST_PARAM);
                if(optional.isPresent()){
                    Optional<Expression> valueOptional = AnnotationHelper.getAnyAttribute(optional.get(), "value", "name");
                    if(valueOptional.isPresent()){
                        key = String.valueOf(ExpressionHelper.getValue(valueOptional.get()));
                    }
                    Optional<Expression> defaultValueOptional = AnnotationHelper.getAttribute(optional.get(), "defaultValue");
                    if(defaultValueOptional.isPresent()){
                        defaultValue = ExpressionHelper.getValue(defaultValueOptional.get());
                    }
                    Optional<Expression> requiredOptional = AnnotationHelper.getAttribute(optional.get(), "required");
                    if(requiredOptional.isPresent() && requiredOptional.get().isBooleanLiteralExpr()){
                        required = requiredOptional.get().asBooleanLiteralExpr().getValue();
                    }
                }

                TypeDescription description = TypeResolvers.resolve(parameter.getType());
                if(description.isAvailable()){
                    section.getParamTag(key).ifPresent(tag->description.addRemark(tag.getContent()));
                    if(required!=null){
                        description.setRequired(required);
                    }
                    if(description.isObject()){
                        objectTypeDescription.merge(description.asObject());
                    }else{
                        description.setKey(key);
                        if(defaultValue!=null && (description.isPrimitive() || description.isString())){
                            description.setDefaultValue(defaultValue);
                        }
                        objectTypeDescription.add(description);
                    }
                }
            }
        }
        section.setParameter(objectTypeDescription.getValue());
        section.addRequestRows(objectTypeDescription.rows());
    }

    /**
     * 解析方法返回参数
     * @param n
     * @param chapter
     * @param section
     */
    private void visitReturn(MethodDeclaration n, Chapter chapter, Section section) {
        TypeDescription description = TypeResolvers.resolve(n.getType());
        if(description.isAvailable()){
            if(description.isPrimitive()){
                section.setRawResponse(description.getValue());
            }else if(description.isString()){
                section.setRawResponse(description.getValue());
            }else if(description.isArray()){
                section.setResponse(description.asArray().getValue());
            }else if(description.isObject()){
                section.setResponse(description.asObject().getValue());
            }
            section.addResponseRows(description.rows());
        }
    }

}
