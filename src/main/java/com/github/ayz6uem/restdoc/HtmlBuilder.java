package com.github.ayz6uem.restdoc;

public class HtmlBuilder implements RestDocVisitor {


    public void build(){
//        Options options = OptionsBuilder.options()
//                .mkDirs(true)
//                .toDir(new File(enviroment.getRestdocPath()))
//                .safe(SafeMode.UNSAFE)
//                .get();
//        AsciiDocDirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(enviroment.getAdocPath());
//        Asciidoctor.Factory.create().convertDirectory(directoryWalker,options);
    }

    @Override
    public void visit(RestDoc restDoc) {

    }
}
