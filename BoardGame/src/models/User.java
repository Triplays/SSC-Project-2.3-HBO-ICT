package models;

public class User {
    private static String username;

    public static void create(String name) {
        username = name;
    }

    public static String get_username() {
        return username;
    }
}
