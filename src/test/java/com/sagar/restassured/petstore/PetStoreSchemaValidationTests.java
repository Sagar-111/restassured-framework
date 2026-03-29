package com.sagar.restassured.petstore;

import com.sagar.restassured.base.BaseTest;
import com.sagar.restassured.constants.Endpoints;
import com.sagar.restassured.utils.PetPayloadBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("Petstore API")
@Feature("Schema Validation")
public class PetStoreSchemaValidationTests extends BaseTest {

    @Test
    @Story("Pet Schema Validation")
    @Description("Verify GET pet by ID response matches defined JSON schema")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetResponseSchema() {
        // Step 1 - get a valid pet ID dynamically
        long petId = given(petstoreSpec)
                .queryParam("status", "available")
                .when()
                .get(Endpoints.GET_PET_BY_STATUS)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id", Long.class)
                .get(0);

        // Step 2 - validate schema of GET /pet/{id} response
        given(petstoreSpec)
                .pathParam("petId", petId)
                .when()
                .get(Endpoints.GET_PET_BY_ID)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("testdata/pet_schema.json"));
    }

    @Test
    @Story("Pet Schema Validation")
    @Description("Verify POST add pet response matches defined JSON schema")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddPetResponseSchema() {
        long petId = System.currentTimeMillis();

        given(petstoreSpec)
                .body(PetPayloadBuilder.buildAddPetPayload(petId, "SchemaTestDog", "available"))
                .when()
                .post(Endpoints.ADD_PET)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("testdata/pet_schema.json"));
    }
}