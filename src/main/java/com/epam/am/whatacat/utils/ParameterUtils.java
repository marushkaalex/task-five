package com.epam.am.whatacat.utils;

public class ParameterUtils {
    public static Long parseLong(String parameter, Long defaultValue) {
        try {
            return Long.parseLong(parameter);
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    public static Integer parseInt(String parameter, Integer defaultValue) {
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }
}
