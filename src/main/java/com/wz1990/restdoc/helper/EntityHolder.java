package com.wz1990.restdoc.helper;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import java.util.*;

/**
 * 存储源代码类结构
 */
public class EntityHolder {

    Table<String, String, Entity> entitySchema = HashBasedTable.create();

    public void put(String name, String fullName, Entity schema) {
        entitySchema.put(name, fullName, schema);
    }

    public Entity getByName(String name) {
        Iterator<Map.Entry<String, Entity>> rows = entitySchema.row(name).entrySet().iterator();
        if (rows.hasNext()) {
            return rows.next().getValue();
        }
        return null;
    }

    public Entity getByFullName(String fullName) {
        Iterator<Map.Entry<String, Entity>> rows = entitySchema.column(fullName).entrySet().iterator();
        if (rows.hasNext()) {
            return rows.next().getValue();
        }
        return null;

    }

    public void linkParent() {
        Iterator<Table.Cell<String, String, Entity>> iterator = entitySchema.cellSet().iterator();
        while (iterator.hasNext()) {
            Table.Cell<String, String, Entity> next = iterator.next();
            Entity v = next.getValue();
            v.setFields(getAllFields(v.getName()));
        }

    }

    public LinkedList<Entity.Field> getAllFields(String name) {
        Entity entity = getByName(name);
        if (entity == null){
            return null;
        }
        LinkedList<Entity.Field> list = null;
        if(Objects.nonNull(entity.getParentName())){
            list = getAllFields(entity.getParentName());
        }
        if(Objects.nonNull(list)){
            list.addAll(entity.getFields());
            return list;
        }else{
            return entity.getFields();
        }
    }
}
