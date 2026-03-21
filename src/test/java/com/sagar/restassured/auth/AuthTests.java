package com.sagar.restassured.auth;

import com.sagar.restassured.base.BaseTest;
import com.sagar.restassured.config.ConfigReader;
import com.sagar.restassured.constants.Endpoints;
import com.sagar.restassured.utils.AuthPayloadBuilder;
import io.qameta.allure.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("ReqRes API")
@Feature("Authentication & Authorization")
public class AuthTests extends BaseTest {

    private static String authToken;

    @Test
    @Story("Authentication")
    @Description("Verify successful login returns a token")
    @Severity(SeverityLevel.BLOCKER)
    public void testSuccessfulLogin() {
        authToken = given(reqresSpec)
                .body(AuthPayloadBuilder.buildLoginPayload(
                        ConfigReader.getAuthEmail(),
                        ConfigReader.getAuthPassword()))
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .jsonPath()
                .getString("token");

        System.out.println("Extracted Token: " + authToken);
    }

    @Test(dependsOnMethods = "testSuccessfulLogin")
    @Story("Authentication")
    @Description("Verify that extracted token can be used as Bearer token in subsequent request")
    @Severity(SeverityLevel.BLOCKER)
    public void testUseTokenInHeader() {
        // Build a new spec with Authorization header injected
        RequestSpecification authenticatedSpec = new RequestSpecBuilder()
                .addRequestSpecification(reqresSpec)
                .addHeader("Authorization", "Bearer " + authToken)
                .build();

        // ReqRes /api/users is a protected-style endpoint we can verify headers with
        given(authenticatedSpec)
                .queryParam("page", 1)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("data", not(empty()));
    }

    @Test
    @Story("Authentication")
    @Description("Verify that login with invalid credentials returns 400")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithInvalidCredentials() {
        given(reqresSpec)
                .body(AuthPayloadBuilder.buildLoginPayload(
                        "invalid@email.com",
                        "wrongpassword"))
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .statusCode(400)
                .body("error", notNullValue());
    }

    @Test
    @Story("Registration")
    @Description("Verify successful registration returns a token")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulRegistration() {
        given(reqresSpec)
                .body(AuthPayloadBuilder.buildRegisterPayload(
                        "eve.holt@reqres.in",
                        "pistol"))
                .when()
                .post(Endpoints.REGISTER)
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("id", notNullValue());
    }

    @Test
    @Story("Registration")
    @Description("Verify that registration without password returns 400 with error message")
    @Severity(SeverityLevel.NORMAL)
    public void testRegistrationWithMissingCredentials() {
        given(reqresSpec)
                .body(AuthPayloadBuilder.buildEmptyPayload())
                .when()
                .post(Endpoints.REGISTER)
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing email or username"));
    }
}