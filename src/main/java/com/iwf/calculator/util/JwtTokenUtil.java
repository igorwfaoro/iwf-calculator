package com.iwf.calculator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.util.Base64;

@Component
public class JwtTokenUtil implements Serializable {

    public static JsonNode getTokenPayloadJsonNode(String token) throws JsonProcessingException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        return JsonUtil.deserializeToJsonNode(new String(decoder.decode(chunks[1])));
    }

    public static JsonNode getTokenPayloadJsonNode() throws JsonProcessingException {
        return getTokenPayloadJsonNode(getTokenFromHeader());
    }

    public static <T> T getTokenPayload(String token, Class<T> className) throws JsonProcessingException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        return JsonUtil.deserialize(new String(decoder.decode(chunks[1])), className);
    }

    public static <T> T getTokenPayload(Class<T> className) throws JsonProcessingException {
        return getTokenPayload(getTokenFromHeader(), className);
    }

    public static String getTokenFromHeader() {
        String rawToken = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return rawToken != null ? rawToken.split(" ")[1] : null;
    }
}
