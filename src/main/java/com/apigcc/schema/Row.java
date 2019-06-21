package com.apigcc.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@EqualsAndHashCode
@NoArgsConstructor
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
