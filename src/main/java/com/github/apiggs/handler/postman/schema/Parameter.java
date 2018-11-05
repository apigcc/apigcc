package com.github.apiggs.handler.postman.schema;


import com.github.apiggs.util.Cell;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Parameter{

    String key;
    String type;
    Object value;
    String description;
    boolean disabled = false;

    public static Parameter of(Cell<String> cell) {
        Parameter parameter = new Parameter();
        parameter.setKey(cell.get(0));
        parameter.setType(cell.get(1));
        parameter.setValue(cell.get(2));
        parameter.setDescription(cell.get(3));
        return parameter;
    }

}
