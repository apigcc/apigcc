package com.wz1990.restdoc.springmvc;

public class SpringParameterPathVariableVisitor {

//    public void visit(Item item, Parameter p, ParsedJavadoc parsedJavadoc) {
//        if (p.getType() instanceof ClassOrInterfaceType) {
//            ClassOrInterfaceType type = (ClassOrInterfaceType) p.getType();
//            if (AstUtils.isBaseType(type)) {
//                String description = parsedJavadoc.getParams().get(p.getNameAsString());
//                addPathVariable(item.getRequest(), p.getNameAsString(), p.getTypeAsString(), description);
//            }
//        }
//    }
//    private void addPathVariable(Item.Request request, String key, String type, String description) {
//        Item.Parameter parameter = new Item.Parameter();
//        parameter.setKey(key);
//        parameter.setType(type);
//        parameter.setValue(AstUtils.defaultValue(type));
//        parameter.setDescription(description);
//        request.getUrl().getPathVariable().add(parameter);
//    }

}
