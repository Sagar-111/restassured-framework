package com.sagar.restassured.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties(){
        properties=new Properties();
        try(InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")){
            if(is == null){
                throw new RuntimeException("config.properties not found on classpath");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties.",e);
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

    public static String getReqresApiKey() {return get("reqres.api.key");}

    public static String getEnv() {
        return get("env");
    }

}
