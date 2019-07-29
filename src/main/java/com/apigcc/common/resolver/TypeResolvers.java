package com.apigcc.common.resolver;

import com.apigcc.common.description.TypeDescription;
import com.apigcc.common.description.UnAvailableTypeDescription;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TypeResolvers {

    private static ObjectTypeResolver objectTypeResolver = new ObjectTypeResolver();

    /**
     * 类型解析器，顺序很重要，ObjectTypeResolver一定要放在最后
     */
    private static List<TypeResolver> typeResolvers = Lists.newArrayList(
            new PrimitiveTypeResolver(),
            new ArrayTypeResolver(),
            new StringTypeResolver(),
            new CollectionTypeResolver(),
            new DateTypeResolver(),
            new MapTypeResolver(),
            new EnumTypeResolver(),
            new SystemObjectTypeResolver(),
            new SpringComponentTypeResolver(),
            objectTypeResolver
    );

    /**
     * 获取类型信息
     * @param type
     * @return
     */
    public static TypeDescription resolve(Type type){
        try{
            ResolvedType resolvedType = type.resolve();
            return resolve(resolvedType);
        } catch (UnsolvedSymbolException e){
            log.warn("UnsolvedSymbolException:{}",e.getMessage());
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
    public static TypeDescription resolve(ResolvedType type){
        for (TypeResolver typeResolver : typeResolvers) {
            if(typeResolver.accept(type)){
                return typeResolver.resolve(type);
            }
        }
        return new UnAvailableTypeDescription();
    }

}
