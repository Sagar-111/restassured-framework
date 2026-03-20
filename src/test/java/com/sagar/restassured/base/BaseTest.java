package com.sagar.restassured.base;

import com.sagar.restassured.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected static RequestSpecification petstoreSpec;
    protected static RequestSpecification reqresSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeSuite
    public void setupSpec(){

        // Petstore request specification
        petstoreSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getPetstoreBaseUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        // ReqRes request specification
        reqresSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getReqresBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("x-api-key", ConfigReader.getReqresApiKey())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .build();

        // Common response specification
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }

}
