package com.github.apigcc.core.parser;

import com.github.apigcc.core.Apigcc;
import com.github.apigcc.core.description.TypeDescription;
import com.github.apigcc.core.common.helper.OptionalHelper;
import com.github.apigcc.core.common.helper.StringHelper;
import com.github.apigcc.core.schema.Tag;
import com.github.apigcc.core.schema.Chapter;
import com.github.apigcc.core.schema.Node;
import com.github.apigcc.core.schema.Project;
import com.github.apigcc.core.schema.Section;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.Setter;

import java.util.Optional;

public class VisitorParser extends VoidVisitorAdapter<Node> {

    @Setter
    private ParserStrategy parserStrategy;

    @Override
    public void visit(final ClassOrInterfaceDeclaration n, final Node arg) {

        if (arg instanceof Project && parserStrategy.accept(n)) {
            Project project = (Project) arg;
            Chapter chapter = new Chapter();
            n.getFullyQualifiedName().ifPresent(chapter::setId);
            chapter.setName(n.getNameAsString());
            n.getComment().ifPresent(chapter::accept);

            OptionalHelper.any(chapter.tag("book"),chapter.tag("group"))
                    .ifPresent(tag -> chapter.setBookName(tag.getContent()));

            parserStrategy.visit(n, chapter);
            project.addChapter(chapter);
            super.visit(n, chapter);
        }
    }

    @Override
    public void visit(final MethodDeclaration n, final Node arg) {
        if (arg instanceof Chapter && parserStrategy.accept(n)) {
            Chapter chapter = (Chapter) arg;
            Section section = new Section();
            section.setId(n.getNameAsString());
            section.setName(n.getNameAsString());
            section.setIndex(chapter.getSections().size());
            n.getComment().ifPresent(section::accept);

            parserStrategy.visit(n, chapter, section);

            visitReturn(n, section);

            chapter.getSections().add(section);
            super.visit(n, section);
        }
    }



    /**
     * 解析方法返回类型
     * @param n
     * @param section
     */
    private void visitReturn(MethodDeclaration n, Section section) {
        Optional<Tag> optional = section.tag("return");
        if(optional.isPresent()){
            //解析@return标签
            String content = optional.get().getContent();
            if (StringHelper.nonBlank(content)) {
                section.setRawResponse(content);
                return;
            }
        }
        TypeDescription description = Apigcc.getInstance().getTypeResolvers().resolve(n.getType());
        if(description.isAvailable()){
            if(description.isPrimitive() || description.isString()){
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
