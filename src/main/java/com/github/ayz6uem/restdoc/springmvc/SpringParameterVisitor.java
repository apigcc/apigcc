package com.github.ayz6uem.restdoc.springmvc;

public class SpringParameterVisitor {

    private SpringParameterPathVariableVisitor pathVariableVisitor = new SpringParameterPathVariableVisitor();

    private SpringParameterQueryStringVisitor queryStringVisitor = new SpringParameterQueryStringVisitor();

    private SpringParameterRequestBodyVisitor requestBodyVisitor = new SpringParameterRequestBodyVisitor();

    private SpringParameterUrlEncodedVisitor urlEncodedVisitor = new SpringParameterUrlEncodedVisitor();

//    public void visit(Parameter p, Item item, ParsedJavadoc parsedJavadoc, RestDoc restDoc){
//        if(p.isAnnotationPresent("PathVariable")){
//            pathVariableVisitor.visit(item, p, parsedJavadoc);
//        }else if(item.getRequest().getMethod().equals(Method.GET)) {
//            queryStringVisitor.visit(item, p, parsedJavadoc, restDoc);
//        }else{
//            if (p.isAnnotationPresent("RequestBody")) {
//                requestBodyVisitor.visit(item, p, restDoc);
//            }else{
//                urlEncodedVisitor.visit(item, p, parsedJavadoc, restDoc);
//            }
//        }
//    }

}
