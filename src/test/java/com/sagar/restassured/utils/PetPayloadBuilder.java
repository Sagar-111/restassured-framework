package com.sagar.restassured.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PetPayloadBuilder {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode buildAddPetPayload(long id, String name, String status) {
        ObjectNode pet = objectMapper.createObjectNode();
        pet.put("id", id);
        pet.put("name", name);
        pet.put("status", status);

        // Category
        ObjectNode category = objectMapper.createObjectNode();
        category.put("id", 1);
        category.put("name", "Dogs");
        pet.set("category", category);

        // PhotoUrls
        ArrayNode photoUrls = objectMapper.createArrayNode();
        photoUrls.add("https://example.com/photo1.jpg");
        pet.set("photoUrls", photoUrls);

        // Tags
        ArrayNode tags = objectMapper.createArrayNode();
        ObjectNode tag = objectMapper.createObjectNode();
        tag.put("id", 1);
        tag.put("name", "vaccinated");
        tags.add(tag);
        pet.set("tags", tags);

        return pet;
    }
}