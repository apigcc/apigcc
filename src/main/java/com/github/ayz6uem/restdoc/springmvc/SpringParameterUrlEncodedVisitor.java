package com.github.ayz6uem.restdoc.springmvc;

public class SpringParameterUrlEncodedVisitor {

//    public void visit(Item item, Parameter p, ParsedJavadoc parsedJavadoc, RestDoc restDoc) {
//
//        item.getRequest().getBody().setMode(BodyMode.urlencoded);
//        item.getRequest().getHeader().add(Item.Header.APPLICATION_URLENCODED);
//
//        if (p.getType() instanceof ClassOrInterfaceType) {
//            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) p.getType();
//            if (AstUtils.isBaseType(classOrInterfaceType)) {
//                String description = parsedJavadoc.getParams().get(p.getNameAsString());
//                addUrlEncodedParameter(item.getRequest(), p.getNameAsString(), p.getTypeAsString(), description);
//            } else {
//                Entity entity = restDoc.getEntityHolder().getByName(classOrInterfaceType.getNameAsString());
//                if (Objects.nonNull(entity)) {
//                    for (int i = 0; i < entity.getFields().size(); i++) {
//                        Entity.Field field = entity.getFields().get(i);
//                        addUrlEncodedParameter(item.getRequest(), field.getName(), field.getType(), field.getDescription());
//                    }
//                }
//            }
//        }
//    }
//
//    private void addUrlEncodedParameter(Item.Request request, String key, String type, String description) {
//        Item.Parameter parameter = new Item.Parameter();
//        parameter.setKey(key);
//        parameter.setType(type);
//        parameter.setValue(AstUtils.defaultValue(type));
//        parameter.setDescription(description);
//        request.getBody().getUrlencoded().add(parameter);
//    }

}
