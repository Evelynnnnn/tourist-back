package com.shilei.tourist.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetPropertiesUtil {

     static String getUrl(String urlName){
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream(new File("src/main/resources/property/Urls.properties")));
        }catch(Exception exception){
            exception.printStackTrace();
        }
        String URL = properties.getProperty(urlName);

        return URL;
    }

    public static String getPushAddress() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("src/main/resources/property/Urls.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String URL = properties.getProperty("pushAddress");
        return URL;
    }

    public static String getApi(String urlName){
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream(new File("src/main/resources/property/Urls.properties")));
        }catch(Exception exception){
            exception.printStackTrace();
        }
        String URL = properties.getProperty(urlName);

        return URL;
    }
}
