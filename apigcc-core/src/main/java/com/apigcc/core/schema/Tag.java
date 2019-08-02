package com.apigcc.core.schema;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * //@param username 用户名
 *    id    key      content
 */
@Slf4j
@Setter
@Getter
public class Tag {

    String id;
    String key;
    String content;

    public int getIntContent(int def){
        if (Objects.nonNull(content)) {
            try{
                return Integer.parseInt(content);
            }catch (Exception e){
                log.warn(content+" parse error");
            }
        }
        return def;
    }

}
