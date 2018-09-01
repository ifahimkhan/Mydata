package com.example.android.todolist;

/**
 * Created by root on 17/7/18.
 */

public class Common {
    private static int id;
    private static int priority;
    private static String description;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Common.id = id;
    }

    public static int getPriority() {
        return priority;
    }

    public static void setPriority(int priority) {
        Common.priority = priority;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        Common.description = description;
    }
}
