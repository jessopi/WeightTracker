package main.java.dbutility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    //private static final String SQCONN = "jdbc:sqlite:src/main/resources/trackWeight.sqlite";

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
