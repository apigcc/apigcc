package com.github.apigcc.springmvc;

import com.github.apigcc.core.Apigcc;
import com.github.apigcc.core.description.TypeDescription;
import com.github.apigcc.core.description.UnAvailableTypeDescription;
import com.github.apigcc.core.common.helper.ClassHelper;
import com.github.apigcc.core.resolver.NameResolver;
import com.github.apigcc.core.resolver.ReferenceTypeResolver;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Optional;

public class SpringComponentResolver extends ReferenceTypeResolver implements NameResolver {

    @Override
    public boolean accept(ResolvedType type) {
        return super.accept(type) && isSpringComponent(type.asReferenceType().getId());
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {

        Optional<ResolvedType> optional = ClassHelper.getTypeParameter(type.asReferenceType(), 0);
        return optional
                .map(Apigcc.getInstance().getTypeResolvers()::resolve)
                .orElseGet(UnAvailableTypeDescription::new);
    }

    @Override
    public boolean accept(String id) {
        return isSpringComponent(id);
    }

    @Override
    public TypeDescription resolve(Type type) {
        if (type.isClassOrInterfaceType()) {
            Optional<NodeList<Type>> optional = type.asClassOrInterfaceType().getTypeArguments();
            if (optional.isPresent() && optional.get().size() > 0) {
                Type typeArgument = optional.get().get(0);
                return Apigcc.getInstance().getTypeResolvers().resolve(typeArgument);
            }

        }
        return new UnAvailableTypeDescription();
    }

    private static boolean isSpringComponent(String id) {
        return id.startsWith("org.springframework");
    }
}
