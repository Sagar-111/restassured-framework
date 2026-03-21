package com.sagar.restassured.constants;

public class Endpoints {

    // Petstore endpoints
    public static final String GET_PET_BY_STATUS = "/pet/findByStatus";
    public static final String GET_PET_BY_ID = "/pet/{petId}";
    public static final String ADD_PET = "/pet";
    public static final String UPDATE_PET = "/pet";
    public static final String DELETE_PET = "/pet/{petId}";

    // Petstore order endpoints
    public static final String PLACE_ORDER = "/store/order";
    public static final String GET_ORDER_BY_ID = "/store/order/{orderId}";
    public static final String DELETE_ORDER = "/store/order/{orderId}";

    // ReqRes auth endpoints
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";

}