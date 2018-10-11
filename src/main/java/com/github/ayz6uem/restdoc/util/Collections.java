package com.github.ayz6uem.restdoc.util;

import java.util.*;

public class Collections {

    public static <T> Set<T> set(T ... t){
        Set<T> set = new HashSet<>(t.length);
        set.addAll(Arrays.asList(t));
        return set;
    }

}
