package com.apigcc.common.description;

import com.apigcc.schema.Row;

import java.util.Collection;

/**
 * 未知类型，应该忽略
 */
public class UnAvailableTypeDescription extends TypeDescription {

    public UnAvailableTypeDescription() {
    }

    @Override
    public Object getValue() {
        throw new IllegalArgumentException("unAvailable type not support");
    }

    @Override
    public Collection<Row> rows() {
        throw new IllegalArgumentException("unAvailable type not support");
    }
}
