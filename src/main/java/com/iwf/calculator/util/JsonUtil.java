package com.iwf.calculator.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
public class JsonUtil implements Serializable {

    public static JsonNode deserializeToJsonNode(String json) throws JsonProcessingException {
        var objectMapper = getMapper();
        return objectMapper.readTree(json);
    }

    public static <T> T deserialize(String json, Class<T> className) throws JsonProcessingException {
        var objectMapper = getMapper();
        return objectMapper.readValue(json, className);
    }

    public static String serialize(Object object) throws JsonProcessingException {
        var objectMapper = getMapper();
        return objectMapper.writeValueAsString(object);
    }

    private static ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }
}
