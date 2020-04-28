package com.github.apigcc.core.resolver;

import com.github.apigcc.core.description.TypeDescription;
import com.github.apigcc.core.description.UnAvailableTypeDescription;
import com.github.apigcc.core.common.helper.ClassHelper;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TypeResolvers {

    /**
     * 最终进行的类型解析器
     */
    ObjectTypeResolver finalTypeResolver = new ObjectTypeResolver();

    /**
     * 类型解析器
     * 顺序很重要，ObjectTypeResolver一定放到最后
     */
    private List<TypeResolver> resolvers = Lists.newArrayList(
            new PrimitiveTypeResolver(),
            new ArrayTypeResolver(),
            new StringTypeResolver(),
            new CollectionTypeResolver(),
            new DateTypeResolver(),
            new MapTypeResolver(),
            new EnumTypeResolver(),
            new SystemObjectTypeResolver()

    );

    private List<NameResolver> nameResolvers = Lists.newArrayList();

    /**
     * 获取类型信息
     *
     * @param type
     * @return
     */
    public TypeDescription resolve(Type type) {
        try {
            ResolvedType resolvedType = type.resolve();
            return resolve(resolvedType);
        } catch (UnsolvedSymbolException e) {
            //解析失败时，尝试降级，使用名称解析
            return resolveByName(type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new UnAvailableTypeDescription();
    }

    /**
     * 解析类型信息
     *
     * @param type
     * @return
     */
    public TypeDescription resolve(ResolvedType type) {
        for (TypeResolver typeResolver : resolvers) {
            if (typeResolver.accept(type)) {
                return typeResolver.resolve(type);
            }
        }

        if (finalTypeResolver.accept(type)) {
            return finalTypeResolver.resolve(type);
        }

        return new UnAvailableTypeDescription();
    }

    /**
     * 通过名称解析类型信息
     *
     * @param type
     * @return
     */
    public TypeDescription resolveByName(Type type) {
        String id = ClassHelper.getId(type);
        for (NameResolver nameResolver : nameResolvers) {
            if (nameResolver.accept(id)) {
                return nameResolver.resolve(type);
            }
        }
        log.warn("type({}) resolve failed", id);
        return new UnAvailableTypeDescription();
    }

    public void addResolver(TypeResolver typeResolver) {
        resolvers.add(typeResolver);
    }

    public void addNameResolver(NameResolver nameResolver) {
        nameResolvers.add(nameResolver);
    }


}
