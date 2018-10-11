package com.github.ayz6uem.restdoc.ast;


/**
 * 存储源代码类结构
 */
public class AstTypeHolder {

//    Map<String, AstType> entitySchema = new HashMap<>();
//
//    public void put(String name, String fullName, AstType schema) {
//        entitySchema.put(name, fullName, schema);
//    }

//    public AstType getByName(String name) {
//        Iterator<Map.Entry<String, AstType>> rows = entitySchema.row(name).entrySet().iterator();
//        if (rows.hasNext()) {
//            return rows.next().getValue();
//        }
//        return null;
//    }
//
//    public AstType getByFullName(String fullName) {
//        Iterator<Map.Entry<String, AstType>> rows = entitySchema.column(fullName).entrySet().iterator();
//        if (rows.hasNext()) {
//            return rows.next().getValue();
//        }
//        return null;
//
//    }
//
//    public void linkParent() {
//        Iterator<Table.Cell<String, String, AstType>> iterator = entitySchema.cellSet().iterator();
//        while (iterator.hasNext()) {
//            Table.Cell<String, String, AstType> next = iterator.next();
//            AstType v = next.getValue();
//            v.setFields(getAllFields(v.getName()));
//        }
//
//    }
//
//    public LinkedList<AstType.Field> getAllFields(String name) {
//        AstType entity = getByName(name);
//        if (entity == null){
//            return null;
//        }
//        LinkedList<AstType.Field> list = null;
//        if(Objects.nonNull(entity.getParentName())){
//            list = getAllFields(entity.getParentName());
//        }
//        if(Objects.nonNull(list)){
//            list.addAll(entity.getFields());
//            return list;
//        }else{
//            return entity.getFields();
//        }
//    }
}
