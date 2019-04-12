package models;

public class Opponent {
    private static String username;

    public static void create(String name) {
        if(name.equals("")){
            name = "Speler 2";
        }
        username = name;
    }

    public static String get_username() { return username; }

}
