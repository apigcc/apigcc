package com.apigcc.core;

import com.apigcc.core.schema.Tree;
import com.apigcc.core.handler.AsciidocTreeHandler;
import com.apigcc.core.handler.HtmlTreeHandler;
import com.apigcc.core.handler.TreeHandler;
import com.apigcc.core.handler.postman.PostmanTreeHandler;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

/**
 * should use Apigcc
 */
@Getter
@Setter
public class Context {

    public static final String NAME = "apigcc";

    public static final Integer DEFAULT_NODE_INDEX = 99;

    public static final ThreadLocal<Context> THREAD_LOCAL = new ThreadLocal<>();

    Context() {
        THREAD_LOCAL.set(this);
    }

    public static Context getContext() {
        return THREAD_LOCAL.get();
    }

    public static Apigcc getApigcc() {
        Context context = getContext();
        if (context instanceof Apigcc) {
            return (Apigcc) context;
        }
        throw new IllegalStateException("context error : " + context);
    }

    protected CombinedTypeSolver typeSolver = new CombinedTypeSolver();
    protected Options options;
    protected Tree tree = new Tree();

    protected Collection<TreeHandler> pipeline = Lists.newArrayList(new PostmanTreeHandler(), new AsciidocTreeHandler(), new HtmlTreeHandler());

    protected Set<String> ignoreTypes = Sets.newHashSet();
}
