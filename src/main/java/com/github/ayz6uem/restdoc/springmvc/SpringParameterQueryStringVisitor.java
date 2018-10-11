package com.github.ayz6uem.restdoc.springmvc;

public class SpringParameterQueryStringVisitor {

//    public void visit(Item item, Parameter p, ParsedJavadoc parsedJavadoc, RestDoc restDoc) {
//        if (p.getType() instanceof ClassOrInterfaceType) {
//            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) p.getType();
//            if (AstUtils.isBaseType(classOrInterfaceType)) {
//                String description = parsedJavadoc.getParams().get(p.getNameAsString());
//                addQueryParameter(item.getRequest(), p.getNameAsString(), p.getTypeAsString(), description);
//            } else {
//                Entity entity = restDoc.getEntityHolder().getByName(classOrInterfaceType.getNameAsString());
//                if (Objects.nonNull(entity)) {
//                    for (int i = 0; i < entity.getFields().size(); i++) {
//                        Entity.Field field = entity.getFields().get(i);
//                        addQueryParameter(item.getRequest(), field.getName(), field.getType(), field.getDescription());
//                    }
//                }
//            }
//        }
//    }
//
//    private void addQueryParameter(Item.Request request, String key, String type, String description) {
//        Item.Parameter parameter = new Item.Parameter();
//        parameter.setKey(key);
//        parameter.setType(type);
//        parameter.setValue(AstUtils.defaultValue(type));
//        parameter.setDescription(description);
//        request.getUrl().getQuery().add(parameter);
//    }

}
