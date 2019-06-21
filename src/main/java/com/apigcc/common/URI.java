package com.apigcc.common;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode
public class URI {

    private String text;

    private URI next;

    public URI() {
    }

    public URI(String text) {
        this.text = text;
    }

    public URI add(String text){
        return add(new URI(text));
    }

    public URI add(URI uri){
        if(next!=null){
            next.add(uri);
        }else{
            next = uri;
        }
        return this;
    }

    public void remove(URI uri){
        if(Objects.equals(next,uri)){
            next = null;
        }else{
            next.remove(uri);
        }
    }

    @Override
    public String toString(){
        List<String> list = new ArrayList<>();
        appendTo(list);
        StringBuilder builder = new StringBuilder();
        for (String text : list) {
            if(StringHelper.nonBlank(text)){
                builder.append("/");
                builder.append(text);
            }
        }
        return builder.toString();
    }

    private void appendTo(List<String> list){
        if(Objects.nonNull(text)){
            list.addAll(Lists.newArrayList(text.split("/")));
        }
        if(next!=null){
            next.appendTo(list);
        }
    }
}
