package Login;

import dbUtility.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    private Connection connection;

    public LoginModel(){
        try{
            this.connection = DbConnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if(this.connection == null){
            System.exit(1);
        }
    }


    public boolean queryForUser(String username){

        String sqlQuery = "SELECT * FROM LoginInformation WHERE Username= '" + username + "'";
        return searchForUser(sqlQuery);
    }

    public boolean queryForUser(String username, String password){

        String sqlQuery = "SELECT * FROM LoginInformation WHERE Username= '" + username + "' AND Password= '" + password +"'";
        return searchForUser(sqlQuery);
    }


    /*
        Passwords should not be stored in plainText. need to use encrpytion (Salt?)
        hashing passwords before store and hashing entered passwords for comparisions.

     */
    public void createAccount(String username,String password){
        String sqlQuery = "INSERT INTO LoginInformation(Username,Password) VALUES ('" + username +"','" + password +"')";

        PreparedStatement statement = null;
        try{
            statement = this.connection.prepareStatement(sqlQuery);
            statement.executeUpdate();

        } catch(SQLException ex) {
            ex.printStackTrace();
        }  finally {
            try{statement.close(); } catch (SQLException e) {}
           }
    }


    private boolean searchForUser(String query)  {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = this.connection.prepareStatement(query);
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
}
