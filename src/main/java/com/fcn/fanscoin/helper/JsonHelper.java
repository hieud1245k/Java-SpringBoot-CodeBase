package com.fcn.fanscoin.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public final class JsonHelper {

    private static final ObjectMapper MAPPER = createObjectMapper();

    private JsonHelper() {
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        return mapper;
    }

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),
                                                                        StandardCharsets.UTF_8);

    public static String convertObjectToString(final Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T readString(final String content, final Class<T> clazz) {
        try {
            return MAPPER.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
