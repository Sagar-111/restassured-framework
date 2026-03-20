package com.sagar.restassured.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_FILE_PATH="config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties(){
        properties=new Properties();
        try(FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)){
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties from path: "+CONFIG_FILE_PATH,e);
        }
    }

    public static String get(String key){
        String value = properties.getProperty(key);
        if(value==null){
            throw new RuntimeException("Property '"+key+"' not found in config.properties");
        }
        return value.trim();
    }

    public static String getPetstoreBaseUrl() {
        return get("petstore.base.url");
    }

    public static String getReqresBaseUrl() {
        return get("reqres.base.url");
    }

    public static String getAuthEmail() {
        return get("auth.email");
    }

    public static String getAuthPassword() {
        return get("auth.password");
    }

    public static String getEnv() {
        return get("env");
    }

}
