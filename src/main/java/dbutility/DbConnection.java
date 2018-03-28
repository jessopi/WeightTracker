package main.java.dbutility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
    Creates a database connection with the embedded sqlite database in package resources.
*/
public class DbConnection {

    public static Connection getConnection() throws SQLException {

        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite::resource:main/resources/trackWeight.sqlite");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
