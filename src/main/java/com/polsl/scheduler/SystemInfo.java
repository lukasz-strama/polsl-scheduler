package com.polsl.scheduler;

/**
 * Utility class for retrieving system and JavaFX runtime information.
 */
public class SystemInfo {

    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

}