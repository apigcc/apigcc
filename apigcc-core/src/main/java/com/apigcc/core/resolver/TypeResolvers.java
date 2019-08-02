package com.apigcc.core.resolver;

import com.apigcc.core.common.description.TypeDescription;
import com.apigcc.core.common.description.UnAvailableTypeDescription;
import com.apigcc.core.common.helper.TypeNameHelper;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TypeResolvers {

    private ObjectTypeResolver objectTypeResolver = new ObjectTypeResolver();

    /**
     * 类型解析器
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

    private List<TypeNameResolver> nameResolvers = Lists.newArrayList();

    /**
     * 获取类型信息
     * @param type
     * @return
     */
    public TypeDescription resolve(Type type){
        try{
            ResolvedType resolvedType = type.resolve();
            return resolve(resolvedType);
        } catch (UnsolvedSymbolException e){
            //解析失败时，尝试降级，使用名称解析
            return nameResolve(type);
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return new UnAvailableTypeDescription();
    }

    /**
     * 解析类型信息
     * @param type
     * @return
     */
    public TypeDescription resolve(ResolvedType type){
        for (TypeResolver typeResolver : resolvers) {
            if(typeResolver.accept(type)){
                return typeResolver.resolve(type);
            }
        }
        if(objectTypeResolver.accept(type)){
            return objectTypeResolver.resolve(type);
        }
        return new UnAvailableTypeDescription();
    }

    public TypeDescription nameResolve(Type type){
        String id = TypeNameHelper.getName(type);
        for (TypeNameResolver nameResolver : nameResolvers) {
            if (nameResolver.accept(id)) {
                return nameResolver.resolve(type);
            }
        }
        log.warn("type({}) resolve failed",id);
        return new UnAvailableTypeDescription();
    }

    public void addResolver(TypeResolver typeResolver){
        resolvers.add(typeResolver);
    }

    public void addNameResolver(TypeNameResolver nameResolver){
        nameResolvers.add(nameResolver);
    }


}
