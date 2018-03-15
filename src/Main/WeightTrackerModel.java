package Main;

import dbUtility.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public void removeQuery(){
        String sqlRemoval = "DELETE FROM weightOverTime";
        removeData(sqlRemoval);
    }
    public void removeQuery(String removalDate){
        String sqlRemoval = "DELETE FROM weightOverTime WHERE Date= '" + removalDate + "'";
        removeData(sqlRemoval);
    }

    private void removeData(String sqlRemoval){
        try{
            Connection conn = DbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlRemoval);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void searchQuery(){
        String sqlInsert = "SELECT * FROM weightOverTime";
        setSearchRange(sqlInsert);
    }

    public void searchQuery(String startingDate,String endingDate){
        String sqlInsert = "SELECT * FROM weightOverTime WHERE Date BETWEEN '" + startingDate + "' AND '" + endingDate +"'";
        setSearchRange(sqlInsert);
    }

    private void setSearchRange(String sqlInsert){
        try{
            Connection conn = DbConnection.getConnection();
            this.weightDataList = FXCollections.observableArrayList();
            ResultSet rs = conn.createStatement().executeQuery(sqlInsert);
            while(rs.next()){
                String d1 = rs.getString(1);
                this.weightDataList.add(new WeightData(d1,rs.getDouble(2)));
            }

            rs.close();
            conn.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertNewData(String date, Double weight){

      String sqlInsert = "INSERT INTO weightOverTime(Date,Weight) VALUES (?,?)";
      try{
            Connection conn = DbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1,date);
            stmt.setDouble(2,weight);
            stmt.executeUpdate();

            stmt.close();
            conn.close();

      } catch(SQLException ex) {
          ex.printStackTrace();
      }
    }

    public ObservableList<WeightData> getWeightDataList() {
        return weightDataList;
    }
}
