package com.apigcc.core.common.description;

public class StringTypeDescription extends TypeDescription {

    public StringTypeDescription(String type, CharSequence charSequence) {
        this.type = type;
        value = charSequence.toString();
    }

    public String getValue(){
        return (String)value;
    }
}
