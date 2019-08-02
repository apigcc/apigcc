package com.apigcc.springmvc.resovler;

import com.apigcc.core.Apigcc;
import com.apigcc.core.common.description.TypeDescription;
import com.apigcc.core.common.description.UnAvailableTypeDescription;
import com.apigcc.core.common.helper.TypeParameterHelper;
import com.apigcc.core.resolver.TypeNameResolver;
import com.apigcc.core.resolver.TypeResolver;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Optional;

public class SpringComponentTypeResolver implements TypeResolver, TypeNameResolver {
    @Override
    public boolean accept(ResolvedType type) {
        return isSpringComponent(type);
    }

    @Override
    public TypeDescription resolve(ResolvedType type) {
        Optional<ResolvedType> optional = TypeParameterHelper.getTypeParameter(type.asReferenceType(), 0);
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
            if (optional.isPresent() && optional.get().size()>0) {
                Type typeArgument = optional.get().get(0);
                return Apigcc.getInstance().getTypeResolvers().resolve(typeArgument);
            }

        }
        return new UnAvailableTypeDescription();
    }

    private static boolean isSpringComponent(ResolvedType type){
        if(!type.isReferenceType()){
            return false;
        }
        return isSpringComponent(type.asReferenceType().getId());
    }

    private static boolean isSpringComponent(String id){
        return id!=null && (id.startsWith("org.springframework"));
    }
}
