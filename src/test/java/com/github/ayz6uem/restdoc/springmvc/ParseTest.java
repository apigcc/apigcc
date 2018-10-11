package com.github.ayz6uem.restdoc.springmvc;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.utils.SourceRoot;
import com.github.ayz6uem.restdoc.RestDoc;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Ignore
public class ParseTest {

    String sourcePath;

    @Before
    public void init(){
        sourcePath = System.getProperty("user.dir");
    }

    @Test
    public void testJavaDoc(){
        String source = "\t/**\r\n" +
                "\t* A模块\r\n" +
                "\t* @param A模块\r\n" +
                "\t*/";
        Javadoc javadoc = JavaParser.parseJavadoc(source);
        javadoc.getDescription().getElements().forEach(element->{
            System.out.println(element.toText());
        });
    }

    @Test
    public void testParseCode(){
        String source = "/** " +
                "* A模块 " +
                "*/ " +
                "@RestController " +
                "public class A{} ";

//        Restdoc restdoc = new Restdoc().parse(source);
//        System.out.println(restdoc.getDocument());
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            System.out.println(objectMapper.writeValueAsString(restdoc.getDocument()));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

//        new RestDoc().parse();
    }

    @Test
    public void testPathResource() throws IOException {
        sourcePath += "/src/test/java/com/wz1990/restdoc/springmvc/controller/UserController.java";
        CompilationUnit cu = JavaParser.parseResource(sourcePath);
        cu.getTypes().forEach(typeDeclaration -> {
            System.out.println(typeDeclaration);
        });
    }

    @Test
    public void testRootPath() throws IOException {
        SourceRoot sourceRoot = new SourceRoot(Paths.get(sourcePath));
        List<ParseResult<CompilationUnit>> list = sourceRoot.tryToParse();
        list.forEach(result->{
            if(result.isSuccessful() && result.getResult().isPresent()){
                CompilationUnit cu = result.getResult().get();
//                cu.getTypes().forEach(typeDeclaration -> {
//                    System.out.println(typeDeclaration.getName());
//                });
//                cu.accept(new CuVisitor(),new RestDoc());
            }
        });
    }

    class CuVisitor extends VoidVisitorAdapter<RestDoc>{

        @Override
        public void visit(TypeExpr n, RestDoc arg) {
            System.out.println("TypeExpr:"+n.getTypeAsString());
            super.visit(n, arg);
        }

        @Override
        public void visit(ClassExpr n, RestDoc arg) {
            System.out.println("ClassExpr:"+n.getTypeAsString());
            super.visit(n, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, RestDoc arg) {
            System.out.println("ClassOrInterfaceDeclaration:"+n.getName()+" inner:"+n.isInnerClass());
            super.visit(n, arg);
        }

        @Override
        public void visit(ModuleDeclaration n, RestDoc arg) {
            System.out.println("ModuleDeclaration:"+n.getName());
            super.visit(n, arg);
        }
    }

}
