package com.example.nymble.task.utils;

import java.util.UUID;

public class Helper {
    public static String getId() {
        return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
    }

    public static boolean isBlank(String name) {
        if (name.isBlank()) {
            return true;
        }
        return false;
    }

    public static boolean getLength(String name) {
        if (name.length() >= 30) {
            return true;
        } else return false;
    }
}
