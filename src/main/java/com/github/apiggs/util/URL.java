package com.github.apiggs.util;

import java.nio.file.Paths;

public class URL {

    /**
     * 格式化链接地址
     *
     * @param parent
     * @param sub
     * @return
     */
    public static String normalize(Object parent, String sub) {
        if (parent instanceof String) {
            return normalize((String) parent, sub);
        }
        return normalize("", sub);
    }

    /**
     * 格式化链接地址
     *
     * @param parent
     * @param sub
     * @return
     */
    public static String normalize(String parent, String sub) {
        if (parent == null) {
            parent = "";
        }
        if (sub == null) {
            sub = "";
        }
        return Paths.get("/", parent, sub).toString();
    }

}
