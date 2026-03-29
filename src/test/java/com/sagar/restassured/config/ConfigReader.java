package com.sagar.restassured.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static String configFileName;

    static {
        loadProperties();
    }

    private static void loadProperties(){
        properties=new Properties();

        String env = System.getProperty("env", "dev");
        configFileName = "config-" + env + ".properties";

        System.out.println("Loading config for environment: " + env);
        System.out.println("Config file: " + configFileName);

        try(InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(configFileName)){
            if(is == null){
                throw new RuntimeException("Config file not found: " + configFileName);
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + configFileName, e);
        }
    }

    public static String get(String key){
        String value = properties.getProperty(key);
        if(value==null){
            throw new RuntimeException("Property '"+key+"' not found in " + configFileName);
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

}
