package com.github.apigcc.core.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Header{

    public static final Header APPLICATION_JSON = new Header("Content-Type","application/json");
    public static final Header X_FORM_WWW_URLENCODED = new Header("Content-Type","x-www-form-urlencoded");

    String key;
    String value;
    String description;

    public Header(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static Header valueOf(String text){
        String[] arr = text.split(":");
        Header header = new Header();
        header.setKey(arr[0]);
        if(arr.length>1){
            header.setValue(arr[1]);
        }
        return header;
    }

    @Override
    public String toString(){
        return key + ": " + value;
    }

}
