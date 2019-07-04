package com.apigcc.schema;

import lombok.*;

import java.util.Objects;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Row {

    String key;
    String type;
    String condition;
    String def;
    String remark;

    public Row(String type) {
        this.type = type;
    }

}
