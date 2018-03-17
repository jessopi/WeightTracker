package Main;

import dbUtility.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import userUtility.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WeightTrackerModel {

    private Connection connection;
    private ObservableList<WeightData> weightDataList;

    public WeightTrackerModel(){
      try{
          this.connection = DbConnection.getConnection();
      } catch (SQLException ex) {
          ex.printStackTrace();
      }
      if(this.connection == null){
          System.exit(1);
      }
    }


    public void removeData(String removalDate){
        PreparedStatement stmt = null;
        Connection conn = null;
        String sqlStatement;
        try{
             conn = DbConnection.getConnection();
            if(removalDate.isEmpty()){
                sqlStatement = "DELETE FROM weightOverTime WHERE User= ?";
                stmt = conn.prepareStatement(sqlStatement);
                stmt.setString(1,UserInfo.getCurrentUser());
            } else {
                sqlStatement = "DELETE FROM weightOverTime WHERE Date= ? AND User= ?";
                stmt = conn.prepareStatement(sqlStatement);
                stmt.setString(1,removalDate);
                stmt.setString(2,UserInfo.getCurrentUser());
            }

            stmt.executeUpdate();

        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            try{stmt.close();}catch (SQLException e){}
            try{conn.close();}catch (SQLException e){}
        }
    }

    //data not sorted by date when pulled only by order added
    public void setSearchRange(String startingDate,String endingDate){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sqlStatement;
        try{
            conn = DbConnection.getConnection();
            if(startingDate.isEmpty() || endingDate.isEmpty()){
                sqlStatement = "SELECT * FROM weightOverTime WHERE User= ?";
                stmt = conn.prepareStatement(sqlStatement);
                stmt.setString(1,UserInfo.getCurrentUser());
            } else {
                sqlStatement = "SELECT * FROM weightOverTime WHERE Date BETWEEN ? AND ? AND User= ?";
                stmt = conn.prepareStatement(sqlStatement);
                stmt.setString(1,startingDate);
                stmt.setString(2,endingDate);
                stmt.setString(3,UserInfo.getCurrentUser());
            }

            this.weightDataList = FXCollections.observableArrayList();
            rs = stmt.executeQuery();

            while(rs.next()){
                String d1 = rs.getString(1);
                this.weightDataList.add(new WeightData(d1,rs.getDouble(2)));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            try{rs.close();}catch (SQLException ex){}
            try{conn.close();}catch (SQLException ex){}
            try{stmt.close();}catch (SQLException ex){}
        }
    }

    public void insertNewData(String date, Double weight){
        Connection conn = null;
        PreparedStatement stmt = null;
      try{
             conn = DbConnection.getConnection();
             stmt = conn.prepareStatement("INSERT INTO weightOverTime(Date,Weight,User) VALUES (?,?,?)");
             stmt.setString(1,date);
             stmt.setDouble(2,weight);
             stmt.setString(3,UserInfo.getCurrentUser());
             stmt.executeUpdate();
      } catch(SQLException e) {
          e.printStackTrace();
      } finally {
          try{ stmt.close(); } catch (SQLException e) {}
          try{ conn.close(); } catch (SQLException e) {}
      }
    }

    public ObservableList<WeightData> getWeightDataList() {
        return weightDataList;
    }
}
