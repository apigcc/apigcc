package com.apigcc.core.resolver;

import com.apigcc.core.common.description.TypeDescription;
import com.github.javaparser.ast.type.Type;

public interface TypeNameResolver {

    boolean accept(String id);

    TypeDescription resolve(Type type);

}
