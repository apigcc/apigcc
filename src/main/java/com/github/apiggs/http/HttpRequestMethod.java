package com.github.apiggs.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The request method of HTTP
 */
@JsonSerialize(using = HttpRequestMethod.Serializer.class)
@JsonDeserialize(using = HttpRequestMethod.Deserializer.class)
public class HttpRequestMethod {

    public static final HttpRequestMethod OPTIONS = new HttpRequestMethod("OPTIONS");

    public static final HttpRequestMethod GET = new HttpRequestMethod("GET");

    public static final HttpRequestMethod HEAD = new HttpRequestMethod("HEAD");

    public static final HttpRequestMethod POST = new HttpRequestMethod("POST");

    public static final HttpRequestMethod PUT = new HttpRequestMethod("PUT");

    public static final HttpRequestMethod PATCH = new HttpRequestMethod("PATCH");

    public static final HttpRequestMethod DELETE = new HttpRequestMethod("DELETE");

    public static final HttpRequestMethod TRACE = new HttpRequestMethod("TRACE");

    private static Map<String, HttpRequestMethod> methods;

    static {
        methods = new HashMap<>();
        methods.put(OPTIONS.toString(), OPTIONS);
        methods.put(GET.toString(), GET);
        methods.put(HEAD.toString(), HEAD);
        methods.put(POST.toString(), POST);
        methods.put(PUT.toString(), PUT);
        methods.put(PATCH.toString(), PATCH);
        methods.put(DELETE.toString(), DELETE);
        methods.put(TRACE.toString(), TRACE);

    }

    public static HttpRequestMethod valueOf(String name) {
        HttpRequestMethod result = methods.get(name);
        return result != null ? result : new HttpRequestMethod(name);
    }

    private final String name;

    /**
     * Creates a new HTTP method with the specified name.  You will not need to
     * create a new method unless you are implementing a protocol derived from
     * HTTP
     */
    public HttpRequestMethod(String name) {
        name = Objects.requireNonNull(name, "name").trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }

        for (int i = 0; i < name.length(); i ++) {
            char c = name.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                throw new IllegalArgumentException("invalid character in name");
            }
        }

        this.name = name;
    }

    /**
     * Returns the name of this method.
     */
    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HttpRequestMethod)) {
            return false;
        }

        HttpRequestMethod that = (HttpRequestMethod) o;
        return name().equals(that.name());
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * 自定义类型序列化
     */
    public static class Serializer extends JsonSerializer<HttpRequestMethod>{

        @Override
        public void serialize(HttpRequestMethod value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeString(value.name());
        }
    }
    
    public static class Deserializer extends JsonDeserializer<HttpRequestMethod>{

        @Override
        public HttpRequestMethod deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return valueOf(p.getText());
        }
    }

}
