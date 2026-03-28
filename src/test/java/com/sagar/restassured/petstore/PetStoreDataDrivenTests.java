package com.sagar.restassured.petstore;

import com.sagar.restassured.base.BaseTest;
import com.sagar.restassured.constants.Endpoints;
import com.sagar.restassured.utils.JsonDataReader;
import com.sagar.restassured.utils.PetPayloadBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Petstore API")
@Feature("Data Driven Pet Tests")
public class PetStoreDataDrivenTests extends BaseTest {

    // ─── Level 1: Inline DataProvider ───

    @DataProvider(name = "petStatusProvider")
    public Object[][] petStatusProvider() {
        return new Object[][] {
                { "available" },
                { "pending" },
                { "sold" }
        };
    }

    @Test(dataProvider = "petStatusProvider")
    @Story("Get Pets by Status")
    @Description("Verify pets are returned for each valid status value")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetsByAllStatuses(String status) {
        given(petstoreSpec)
                .queryParam("status", status)
                .when()
                .get(Endpoints.GET_PET_BY_STATUS)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    // ─── Level 2: External JSON DataProvider ───

    @DataProvider(name = "petJsonDataProvider")
    public Object[][] petJsonDataProvider() {
        List<Object[]> data = JsonDataReader.getPetTestData("pet_test_data.json");
        return data.toArray(new Object[0][]);
    }

    @Test(dataProvider = "petJsonDataProvider")
    @Story("Add Pet with Multiple Datasets")
    @Description("Verify pets can be added using data from external JSON file")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddPetWithExternalData(String name, String status, String categoryName) {
        long petId = System.currentTimeMillis();

        given(petstoreSpec)
                .body(PetPayloadBuilder.buildAddPetPayload(petId, name, status))
                .when()
                .post(Endpoints.ADD_PET)
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("status", equalTo(status));
    }

    // ─── Level 3: DataProvider with multiple parameters inline ───

    @DataProvider(name = "invalidStatusProvider")
    public Object[][] invalidStatusProvider() {
        return new Object[][] {
                { "AVAILABLE" },  // wrong case
                { "unknown" },    // invalid value
                { "" }            // empty string
        };
    }

    @Test(dataProvider = "invalidStatusProvider")
    @Story("Get Pets by Invalid Status")
    @Description("Verify API behaviour for invalid status values")
    @Severity(SeverityLevel.MINOR)
    public void testGetPetsByInvalidStatus(String status) {
        int statusCode = given(petstoreSpec)
                .queryParam("status", status)
                .when()
                .get(Endpoints.GET_PET_BY_STATUS)
                .then()
                .extract()
                .statusCode();

        // Petstore returns 200 with empty list or 400 for invalid status
        assert statusCode == 200 || statusCode == 400
                : "Unexpected status code for invalid status '" + status + "': " + statusCode;
    }
}