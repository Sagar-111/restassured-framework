package com.sagar.restassured.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonDataReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Object[]> getPetTestData(String fileName){
        List<Object[]> data = new ArrayList<>();
        try(InputStream is = JsonDataReader.class
                .getClassLoader()
                .getResourceAsStream("testData/"+fileName)){

            if(is == null){
                throw new RuntimeException("Test data file not found: " + fileName);
            }

            JsonNode rootNode = objectMapper.readTree(is);
            for(JsonNode node : rootNode){
                String name = node.get("name").asText();
                String status = node.get("status").asText();
                String categoryName = node.get("categoryName").asText();

                data.add(new Object[]{name, status, categoryName});
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data: " + fileName, e);
        }

        return data;
    }
}
