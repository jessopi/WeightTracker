package main.java.login;

import main.java.dbutility.DbConnection;
import org.mindrot.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    Model for Login application
    Handles queries for login and account creation
 */
public class AppLoginModel {
    private Connection connection;

    public AppLoginModel(){
        try{
            this.connection = DbConnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if(this.connection == null){
            System.exit(1);
        }
    }

    //Adds new Username & encrypted password to login table in database
    public void createAccount(String username,String password){
        //Encrypts password using generated salt provided by BCrpyt
        String hashed = BCrypt.hashpw(password,BCrypt.gensalt());
        PreparedStatement statement = null;
        try{
            statement = this.connection.prepareStatement("INSERT INTO LoginInformation(Username,Password) VALUES (?,?)");
            statement.setString(1,username.trim().toLowerCase());
            statement.setString(2,hashed);
            statement.executeUpdate();

        } catch(SQLException ex) {
            ex.printStackTrace();
        }  finally {
            try{statement.close(); } catch (SQLException e) {}
           }
    }

    //Given a username searches if that user exists in the table if so return true
    public boolean searchForUser(String username)  {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = this.connection.prepareStatement("SELECT * FROM LoginInformation WHERE Username= ?");
            statement.setString(1,username.trim().toLowerCase());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch(SQLException ex){
            return false;
        } finally {
            try{statement.close(); } catch (SQLException e) {}
            try{resultSet.close(); } catch (SQLException e) {}
        }
    }

    //Given a username and password check that both match the stored username and password if so return true
    public boolean searchForUser(String username, String password)  {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = this.connection.prepareStatement("SELECT * FROM LoginInformation WHERE Username= ?");
            statement.setString(1,username.trim().toLowerCase());
            resultSet = statement.executeQuery();
            if(resultSet.next() && BCrypt.checkpw(password,resultSet.getString(2))){
                return true;
            } else {
                return false;
            }
        } catch(SQLException ex){
            return false;
        } finally {
            try{statement.close(); } catch (SQLException e) {}
            try{resultSet.close(); } catch (SQLException e) {}
        }
    }
}
