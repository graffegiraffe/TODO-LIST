package by.rublevskaya.todolist.service;

import by.rublevskaya.todolist.log.CustomLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsHandler {
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream input = PropsHandler.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (input == null) {
                CustomLogger.error("Config file not found: " + CONFIG_FILE_PATH);
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            CustomLogger.error("Failed to load properties from " + CONFIG_FILE_PATH + ": " + e.getMessage());
        }
    }

    public static String getPropertyFromConfig(String propName) {
        String value = properties.getProperty(propName);
        if (value == null) {
            CustomLogger.error("Property not found: " + propName);
        }
        return value;
    }

    public static void printAllProperties() {
        properties.forEach((key, value) -> {
            System.out.println(key + "=" + value);
        });
    }
}
