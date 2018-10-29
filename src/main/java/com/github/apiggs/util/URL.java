package com.github.apiggs.util;

public class URL {

    /**
     * 格式化链接地址
     * @param parent
     * @param sub
     * @return
     */
    public static String normalize(Object parent, String sub) {
        if(parent!=null && parent instanceof String){
            return normalize((String)parent,sub);
        }
        return normalize(null,sub);
    }

    /**
     * 格式化链接地址
     * @param parent
     * @param sub
     * @return
     */
    public static String normalize(String parent, String sub) {
        StringBuilder stringBuilder = new StringBuilder("/");
        if(parent!=null && !"".equals(parent)){
            stringBuilder.append("/").append(parent);
        }
        if(sub!=null && !"".equals(sub)){
            stringBuilder.append("/").append(sub);
        }
        return replaceDoubleLine(stringBuilder.toString());
    }

    private static String replaceDoubleLine(String str){
        if(str.contains("//")){
            str =  str.replaceAll("//","/");
            return replaceDoubleLine(str);
        }
        return str;
    }

}
