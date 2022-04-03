package org.geekbrains.lesson3;

import org.geekbrains.App;

import java.io.IOException;
import java.util.Properties;

public class Utils {

    public static String getConfigValue(String param) {
        Properties prop = new Properties();
        try {
            prop.load(App.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return prop.getProperty(param);
    }
}
