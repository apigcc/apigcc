package com.github.apigcc.core.resolver;

import com.github.apigcc.core.description.TypeDescription;
import com.github.javaparser.ast.type.Type;

public interface NameResolver {

    boolean accept(String id);

    TypeDescription resolve(Type type);

}
