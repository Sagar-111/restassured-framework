package com.sagar.restassured.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AuthPayloadBuilder {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode buildLoginPayload(String email, String password) {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("email", email);
        payload.put("password", password);
        return payload;
    }

    public static ObjectNode buildRegisterPayload(String email, String password) {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("email", email);
        payload.put("password", password);
        return payload;
    }

    public static ObjectNode buildEmptyPayload() {
        return objectMapper.createObjectNode();
    }
}