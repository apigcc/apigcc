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

/**
 * The response code and its description of HTTP
 */
@JsonSerialize(using = HttpResponseStatus.Serializer.class)
@JsonDeserialize(using = HttpResponseStatus.Deserializer.class)
public class HttpResponseStatus {

    public static final HttpResponseStatus OK = newStatus(200, "OK");

    public static final HttpResponseStatus MOVED_PERMANENTLY = newStatus(301, "Moved Permanently");

    public static final HttpResponseStatus FOUND = newStatus(302, "Found");

    public static final HttpResponseStatus SEE_OTHER = newStatus(303, "See Other");

    public static final HttpResponseStatus UNAUTHORIZED = newStatus(401, "Unauthorized");

    public static final HttpResponseStatus FORBIDDEN = newStatus(403, "Forbidden");

    public static final HttpResponseStatus NOT_FOUND = newStatus(404, "Not Found");

    public static final HttpResponseStatus INTERNAL_SERVER_ERROR = newStatus(500, "Internal Server Error");

    public static final HttpResponseStatus BAD_GATEWAY = newStatus(502, "Bad Gateway");

    public static final HttpResponseStatus SERVICE_UNAVAILABLE = newStatus(503, "Service Unavailable");

    public static final HttpResponseStatus GATEWAY_TIMEOUT = newStatus(504, "Gateway Timeout");

    public static final HttpResponseStatus DEFAULT = OK;

    private static HttpResponseStatus newStatus(int statusCode, String reasonPhrase) {
        return new HttpResponseStatus(statusCode, reasonPhrase);
    }

    public static HttpResponseStatus valueOf(int code) {
        switch (code) {
            case 200:
                return OK;
            case 301:
                return MOVED_PERMANENTLY;
            case 302:
                return FOUND;
            case 303:
                return SEE_OTHER;
            case 401:
                return UNAUTHORIZED;
            case 403:
                return FORBIDDEN;
            case 404:
                return NOT_FOUND;
            case 500:
                return INTERNAL_SERVER_ERROR;
            case 502:
                return BAD_GATEWAY;
            case 503:
                return SERVICE_UNAVAILABLE;
            case 504:
                return GATEWAY_TIMEOUT;
        }

        return new HttpResponseStatus(code);
    }

    private final int code;
    private final String reasonPhrase;

    /**
     * Creates a new instance with the specified {@code code} and the auto-generated default reason phrase.
     */
    private HttpResponseStatus(int code) {
        this(code, "(" + code + ")");
    }

    /**
     * Creates a new instance with the specified {@code code} and its {@code reasonPhrase}.
     */
    public HttpResponseStatus(int code, String reasonPhrase) {
        if (code < 0) {
            throw new IllegalArgumentException(
                    "code: " + code + " (expected: 0+)");
        }

        if (reasonPhrase == null) {
            throw new NullPointerException("reasonPhrase");
        }

        for (int i = 0; i < reasonPhrase.length(); i++) {
            char c = reasonPhrase.charAt(i);
            // Check prohibited characters.
            switch (c) {
                case '\n':
                case '\r':
                    throw new IllegalArgumentException(
                            "reasonPhrase contains one of the following prohibited characters: " +
                                    "\\r\\n: " + reasonPhrase);
            }
        }

        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Returns the code of this {@link HttpResponseStatus}.
     */
    public int code() {
        return code;
    }

    /**
     * Returns the reason phrase of this {@link HttpResponseStatus}.
     */
    public String reasonPhrase() {
        return reasonPhrase;
    }


    @Override
    public int hashCode() {
        return code();
    }

    /**
     * Equality of {@link HttpResponseStatus} only depends on {@link #code()}. The
     * reason phrase is not considered for equality.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HttpResponseStatus)) {
            return false;
        }

        return code() == ((HttpResponseStatus) o).code();
    }

    @Override
    public String toString() {
        return new StringBuilder(reasonPhrase.length() + 4)
                .append(code)
                .append(' ')
                .append(reasonPhrase)
                .toString();
    }

    public static class Serializer extends JsonSerializer<HttpResponseStatus>{

        @Override
        public void serialize(HttpResponseStatus httpResponseStatus, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(String.valueOf(httpResponseStatus.code()));
        }
    }

    public static class Deserializer extends JsonDeserializer<HttpResponseStatus>{

        @Override
        public HttpResponseStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return valueOf(jsonParser.getIntValue());
        }
    }

}
