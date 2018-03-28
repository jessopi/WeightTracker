package main.java.application;

import main.java.dbutility.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.userutility.CurrentUser;
import main.java.weightcontainer.CurrentUserData;
import main.java.weightcontainer.WeightData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
    Model class for the main weightTracker application
    Handles sql queries called from controller class.
 */
public class WeightTrackerModel {

    private Connection connection;
    private CurrentUserData currentUserData;

    //Attempts to establish a connection to database
    public WeightTrackerModel(){
      try{
          this.connection = DbConnection.getConnection();
      } catch (SQLException ex) {
          ex.printStackTrace();
      }
      if(this.connection == null){
          System.exit(1);
      }
      currentUserData = new CurrentUserData();
    }

    //Depending if parameter is an empty string or a date, function queries the database and removes
    //all values of current user or just the specific date specified.
    public void removeData(String removalDate){
        PreparedStatement stmt = null;
        Connection conn = null;
        String sqlStatement;
        try{
             conn = DbConnection.getConnection();
            if(removalDate.isEmpty()){
                sqlStatement = "DELETE FROM weightOverTime WHERE User= ?";
                stmt = conn.prepareStatement(sqlStatement);
                stmt.setString(1, CurrentUser.getCurrentUser());
            } else {
                sqlStatement = "DELETE FROM weightOverTime WHERE Date= ? AND User= ?";
                stmt = conn.prepareStatement(sqlStatement);
                stmt.setString(1,removalDate);
                stmt.setString(2, CurrentUser.getCurrentUser());
            }

            stmt.executeUpdate();

        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            try{stmt.close();}catch (SQLException e){}
            try{conn.close();}catch (SQLException e){}
        }
    }

    //If passed in strings are empty then loads all data of that current user else
    //loads data that falls into the range specified.
    public void setSearchRange(String startingDate,String endingDate){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sqlStatement;
        this.currentUserData.clear();
        try{
            conn = DbConnection.getConnection();
            if(startingDate.isEmpty() || endingDate.isEmpty()){
                sqlStatement = "SELECT * FROM weightOverTime WHERE User= ? ORDER BY Date ASC";
                stmt = conn.prepareStatement(sqlStatement);
                stmt.setString(1, CurrentUser.getCurrentUser());
            } else {
                sqlStatement = "SELECT * FROM weightOverTime WHERE Date BETWEEN ? AND ? AND User= ? ORDER BY Date ASC";
                stmt = conn.prepareStatement(sqlStatement);
                stmt.setString(1,startingDate);
                stmt.setString(2,endingDate);
                stmt.setString(3, CurrentUser.getCurrentUser());
            }
            rs = stmt.executeQuery();

            while(rs.next()){
                String d1 = rs.getString(1);
                this.currentUserData.addData(new WeightData(d1,rs.getDouble(2)));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            try{rs.close();}catch (SQLException ex){}
            try{conn.close();}catch (SQLException ex){}
            try{stmt.close();}catch (SQLException ex){}
        }
    }

    //Inserts a new date and weight into the database.
    public void insertNewData(String date, Double weight){
        Connection conn = null;
        PreparedStatement stmt = null;
      try{
             conn = DbConnection.getConnection();
             stmt = conn.prepareStatement("INSERT INTO weightOverTime(Date,Weight,User) VALUES (?,?,?)");
             stmt.setString(1,date);
             stmt.setDouble(2,weight);
             stmt.setString(3, CurrentUser.getCurrentUser());
             stmt.executeUpdate();
      } catch(SQLException e) {
          e.printStackTrace();
      } finally {
          try{ stmt.close(); } catch (SQLException e) {}
          try{ conn.close(); } catch (SQLException e) {}
      }
    }

    //returns ObservableList used to populate linegraph and table.
    public ObservableList<WeightData> getWeightDataList() {
        ObservableList<WeightData> observableList = FXCollections.observableList(this.currentUserData.getWeightDataArrayList());
        return observableList;
    }

    //returns list of information based on data range
    public List<String> getAdditionalInfo(){

        List<String> info = new ArrayList<>();
        info.add("Number of Entries:   " + currentUserData.numberOfEntries());
        info.add("Search Range:   " + currentUserData.getSearchRange());
        info.add("Maximum Weight:   " + currentUserData.getMax() + " lbs.");
        info.add("Minimum Weight:   " + currentUserData.getMin()+ " lbs.") ;
        info.add("Average Weight:   " + currentUserData.getAverage() + " lbs.");
        info.add("Greatest Fluctuation in Weight:   " + currentUserData.getLargestFluctuation() + " lbs.");

        return info;
    }

}
