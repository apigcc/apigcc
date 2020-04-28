package com.github.apigcc.core.common.helper;

import java.util.Optional;

public class OptionalHelper {

    @SafeVarargs
    public static <T> Optional<T> any(Optional<T> ... optionals){
        for (Optional<T> optional : optionals) {
            if(optional.isPresent()){
                return optional;
            }
        }
        return Optional.empty();
    }

}
