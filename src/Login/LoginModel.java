package Login;

import dbUtility.DbConnection;
import org.mindrot.BCrypt;

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

    public void createAccount(String username,String password){
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
