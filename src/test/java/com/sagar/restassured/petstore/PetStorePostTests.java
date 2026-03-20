package com.sagar.restassured.petstore;

import com.sagar.restassured.base.BaseTest;
import com.sagar.restassured.constants.Endpoints;
import com.sagar.restassured.utils.PetPayloadBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetStorePostTests extends BaseTest {

    private long petId;
    private String petName;
    private String petStatus;

    @BeforeClass
    public void setupTestData() {
        // Using timestamp as ID to avoid conflicts with other users on public API
        petId = System.currentTimeMillis();
        petName = "TestDog_" + petId;
        petStatus = "available";
    }

    @Test
    @Story("Add New Pet")
    @Description("Verify that a new pet can be added successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddNewPet() {
        given(petstoreSpec)
                .body(PetPayloadBuilder.buildAddPetPayload(petId, petName, petStatus))
                .when()
                .post(Endpoints.ADD_PET)
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("name", equalTo(petName))
                .body("status", equalTo(petStatus));
    }

    @Test(dependsOnMethods = "testAddNewPet")
    @Story("Add New Pet")
    @Description("Verify that the newly added pet can be retrieved")
    @Severity(SeverityLevel.CRITICAL)
    public void testVerifyAddedPet() {
        given(petstoreSpec)
                .pathParam("petId", petId)
                .when()
                .get(Endpoints.GET_PET_BY_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("name", equalTo(petName));
    }

    @Test(dependsOnMethods = "testVerifyAddedPet")
    @Story("Update Pet")
    @Description("Verify that an existing pet can be updated successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdatePet() {
        String updatedStatus = "sold";
        String updatedName = "UpdatedDog_" + petId;

        given(petstoreSpec)
                .body(PetPayloadBuilder.buildAddPetPayload(petId, updatedName, updatedStatus))
                .when()
                .put(Endpoints.UPDATE_PET)
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedName))
                .body("status", equalTo(updatedStatus));
    }

    @Test(dependsOnMethods = "testUpdatePet")
    @Story("Delete Pet")
    @Description("Verify that an existing pet can be deleted successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeletePet() {
        given(petstoreSpec)
                .pathParam("petId", petId)
                .when()
                .delete(Endpoints.DELETE_PET)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "testDeletePet")
    @Story("Delete Pet")
    @Description("Verify that a deleted pet returns 404")
    @Severity(SeverityLevel.NORMAL)
    public void testVerifyDeletedPet() {
        given(petstoreSpec)
                .pathParam("petId", petId)
                .when()
                .get(Endpoints.GET_PET_BY_ID)
                .then()
                .statusCode(404);
    }
}