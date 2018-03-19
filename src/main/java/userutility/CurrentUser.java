package main.java.userutility;

public class CurrentUser {
    private static String currentUser = "";

    public static void setCurrentUser(String user){
        currentUser = user;
    }
    public static String getCurrentUser(){
        return currentUser;
    }
}
