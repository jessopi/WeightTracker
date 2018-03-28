package main.java.userutility;

/*
    Contains current active user.
    Grabs username when login is successful.
*/
public class CurrentUser {
    private static String currentUser = "";

    public static void setCurrentUser(String user){
        currentUser = user;
    }
    public static String getCurrentUser(){
        return currentUser;
    }
}
