package com.github.apigcc.core.resolver;

import com.github.apigcc.core.Apigcc;
import com.github.apigcc.core.description.ArrayTypeDescription;
import com.github.apigcc.core.description.TypeDescription;
import com.github.apigcc.core.description.UnAvailableTypeDescription;
import com.github.apigcc.core.common.helper.ClassHelper;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.ImmutableList;

import java.util.*;

public class CollectionTypeResolver extends ReferenceTypeResolver {

    private static final ImmutableList<String> list = ImmutableList.of(
            List.class.getName(),
            Collection.class.getName(),
            ArrayList.class.getName(),
            LinkedList.class.getName(),
            Set.class.getName(),
            HashSet.class.getName(),
            Iterator.class.getName(),
            Iterable.class.getName()
    );

    @Override
    public boolean accept(ResolvedType type) {
        return super.accept(type) && list.contains(type.asReferenceType().getId());
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        Optional<ResolvedType> optional = ClassHelper.getTypeParameter(type.asReferenceType(), 0);
        return new ArrayTypeDescription(optional
                .map(Apigcc.getInstance().getTypeResolvers()::resolve)
                .orElseGet(UnAvailableTypeDescription::new));
    }

}
