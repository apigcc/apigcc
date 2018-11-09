package com.github.apiggs;

import com.github.apiggs.handler.AsciidocTreeHandler;
import com.github.apiggs.handler.HtmlTreeHandler;
import com.github.apiggs.handler.TreeHandler;
import com.github.apiggs.handler.postman.PostmanTreeHandler;
import com.github.apiggs.schema.Tree;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

/**
 * should use Apiggs
 */
@Getter
@Setter
public class Context {

    public static final String NAME = "apiggs";

    public static final Integer DEFAULT_NODE_INDEX = 99;

    public static final ThreadLocal<Context> THREAD_LOCAL = new ThreadLocal<>();

    Context() {
        THREAD_LOCAL.set(this);
    }

    public static Context getContext() {
        return THREAD_LOCAL.get();
    }

    public static Apiggs getApiggs() {
        Context context = getContext();
        if (context instanceof Apiggs) {
            return (Apiggs) context;
        }
        throw new IllegalStateException("context error : " + context);
    }

    protected CombinedTypeSolver typeSolver = new CombinedTypeSolver();
    protected Options options;
    protected Tree tree = new Tree();

    protected Collection<TreeHandler> pipeline = Lists.newArrayList(new PostmanTreeHandler(), new AsciidocTreeHandler(), new HtmlTreeHandler());

    protected Set<String> ignoreTypes = Sets.newHashSet();
}
