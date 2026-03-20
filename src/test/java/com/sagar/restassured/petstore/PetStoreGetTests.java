package com.sagar.restassured.petstore;

import com.sagar.restassured.base.BaseTest;
import com.sagar.restassured.constants.Endpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetStoreGetTests extends BaseTest {

    @Test
    @Story("Get Pets by Status")
    @Description("Verify that pets with status 'available' are returned successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAvailablePets() {
        given(petstoreSpec)
                .queryParam("status", "available")
                .when()
                .get(Endpoints.GET_PET_BY_STATUS)
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("[0].status", equalTo("available"));
    }

    @Test
    @Story("Get Pet by ID")
    @Description("Verify that a pet can be fetched by a valid ID dynamically retrieved")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetById() {
        // Step 1 - get list of available pet IDs
        List<Long> petIds = given(petstoreSpec)
                .queryParam("status", "available")
                .when()
                .get(Endpoints.GET_PET_BY_STATUS)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id", Long.class);

        // Step 2 - find first ID that actually returns 200
        long validPetId = -1;
        for (long id : petIds) {
            int statusCode = given(petstoreSpec)
                    .pathParam("petId", id)
                    .when()
                    .get(Endpoints.GET_PET_BY_ID)
                    .then()
                    .extract()
                    .statusCode();

            if (statusCode == 200) {
                validPetId = id;
                break;
            }
        }

        // Step 3 - assert we found a valid pet
        assert validPetId != -1 : "No valid pet ID found from available pets list";

        // Step 4 - final assertion
        given(petstoreSpec)
                .pathParam("petId", validPetId)
                .when()
                .get(Endpoints.GET_PET_BY_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(validPetId));
    }

    @Test
    @Story("Get Pet by ID")
    @Description("Verify that fetching a non-existent pet returns 404")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetByInvalidId() {
        given(petstoreSpec)
                .pathParam("petId", 999999999)
                .when()
                .get(Endpoints.GET_PET_BY_ID)
                .then()
                .statusCode(404);
    }

    @Test
    @Story("Get Pets by Status")
    @Description("Verify that pets with status 'sold' are returned successfully")
    @Severity(SeverityLevel.NORMAL)
    public void testGetSoldPets() {
        given(petstoreSpec)
                .queryParam("status", "sold")
                .when()
                .get(Endpoints.GET_PET_BY_STATUS)
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("[0].status", equalTo("sold"));
    }
}
