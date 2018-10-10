package com.wz1990.restdoc.springmvc;

public class SpringResultTypeVisitor {

//    public void visit(MethodDeclaration n, Item item, ParsedJavadoc parsedJavadoc, RestDoc restDoc){
//        Item.Response response = item.getResponse();
//
//        if(n.getType() instanceof ClassOrInterfaceType){
//            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) n.getType();
//            String name = classOrInterfaceType.getNameAsString();
//            Entity entity = restDoc.getEntityHolder().getByName(name);
//            if(Objects.isNull(entity)){
//                return;
//            }
//            addBodyParameter(response, entity, "");
//            ObjectNode resultNode = entity.getJsonNode().deepCopy();
//            for (int i = 0; i < entity.getFields().size(); i++) {
//                Entity.Field field = entity.getFields().get(i);
//                if(entity.isGeneric(field) && classOrInterfaceType.getTypeArguments().isPresent()){
//                    AstGeneric astGeneric = (AstGeneric) field.getValue();
//                    NodeList<Type> types = classOrInterfaceType.getTypeArguments().get();
//                    Type type = types.get(astGeneric.getIndex());
//                    if(type instanceof ClassOrInterfaceType){
//                        ClassOrInterfaceType generic = (ClassOrInterfaceType) type;
//                        Entity genericEntity = restDoc.getEntityHolder().getByName(generic.getNameAsString());
//                        if(Objects.nonNull(genericEntity)){
//                            addBodyParameter(response, genericEntity, field.getName() + ".");
//                            resultNode.set(field.getName(),genericEntity.getJsonNode());
//                        }
//                    }
//
//                }
//            }
//
//            response.getHeader().add(Item.Header.APPLICATION_JSON);
//            response.setBody(JsonHelper.toPretty(resultNode));
//        }
//    }
//
//    private void addBodyParameter(Item.Response response, Entity entity, String prefix) {
//        for (int i = 0; i < entity.getFields().size(); i++) {
//            Entity.Field field = entity.getFields().get(i);
//            Item.Parameter parameter = new Item.Parameter();
//            parameter.setKey(prefix + field.getName());
//            parameter.setType(field.getType());
//            parameter.setValue(AstUtils.defaultValue(field.getType()));
//            parameter.setDescription(field.getDescription());
//            response.getBodyParameter().add(parameter);
//        }
//    }

}
