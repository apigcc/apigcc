package com.apigcc.core.resolver;

import com.apigcc.core.common.Cell;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
public class Types {

    /**
     * 已解析类型结果池，防止循环递归
     */
    private static Map<Object, Types> POOL = new ConcurrentHashMap<>();

    private Types(){
    }

    public static boolean contain(Object obj){
        return POOL.containsKey(obj);
    }

    public static Types get(Object obj){
        if (POOL.containsKey(obj)) {
            return POOL.get(obj);
        }
        return new Types();
    }

    public static void put(Object obj, Types types){
        POOL.put(obj, types);
    }

    /**
     * 获取解析结果前，应判断是否已解析
     */
    boolean resolved;
    /**
     * 是否基本类型
     */
    boolean primitive;
    String name;
    Object value;
    List<Cell<String>> cells = new ArrayList<>();

    public Types duplicate() {
        Types types = new Types();
        types.name = this.name;
        types.resolved = this.resolved;
        types.primitive = this.primitive;
        types.value = this.value;
        for (Cell<String> cell : this.cells) {
            types.cells.add(cell.duplicate());
        }
        return types;
    }

    public void prefix(String prefix) {
        for (Cell<String> cell : cells) {
            cell.set(0, prefix + cell.get(0));
        }
    }

}
