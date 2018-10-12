package com.github.ayz6uem.restdoc.http;

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

@JsonSerialize(using = HttpVersion.Serializer.class)
@JsonDeserialize(using = HttpVersion.Deserializer.class)
public enum HttpVersion {

    V_1_0("HTTP/1.0"),V_1_1("HTTP/1.1");

    private static Map<String, HttpVersion> all;

    static {
        all = new HashMap<>();
        all.put(V_1_0.toString(), V_1_0);
        all.put(V_1_1.toString(), V_1_1);
    }

    public static HttpVersion valueOfText(String name) {
        return all.get(name);
    }

    public static final HttpVersion DEFAULT = V_1_1;

    private String text;

    HttpVersion(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static class Serializer extends JsonSerializer<HttpVersion>{

        @Override
        public void serialize(HttpVersion httpVersion, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(httpVersion.text);
        }
    }

    public static class Deserializer extends JsonDeserializer<HttpVersion>{

        @Override
        public HttpVersion deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return valueOfText(jsonParser.getText());
        }
    }

}
