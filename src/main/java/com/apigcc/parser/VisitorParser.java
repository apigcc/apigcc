package com.apigcc.parser;

import com.apigcc.common.helper.OptionalHelper;
import com.apigcc.schema.Chapter;
import com.apigcc.schema.Node;
import com.apigcc.schema.Project;
import com.apigcc.schema.Section;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VisitorParser extends VoidVisitorAdapter<Node> {

    private ParserStrategy parserStrategy;

    public void setParserStrategy(ParserStrategy parserStrategy) {
        this.parserStrategy = parserStrategy;
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration n, final Node arg) {

        if (arg instanceof Project && parserStrategy.accept(n)) {
            Project project = (Project) arg;
            Chapter chapter = new Chapter();
            n.getFullyQualifiedName().ifPresent(chapter::setId);
            chapter.setName(n.getNameAsString());
            n.getComment().ifPresent(chapter::accept);

            OptionalHelper.any(chapter.getTag("book"),chapter.getTag("group"))
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
            chapter.getSections().add(section);
            super.visit(n, section);
        }
    }

}
