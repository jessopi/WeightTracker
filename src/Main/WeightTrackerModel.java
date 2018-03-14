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

    public void setSearchRange(String startingDate,String endingDate){
        String sqlInsert = "SELECT * FROM weightOverTime WHERE Date BETWEEN '" + startingDate + "' AND '" + endingDate +"'";
        try{
            Connection conn = DbConnection.getConnection();
            this.weightDataList = FXCollections.observableArrayList();
            ResultSet rs = conn.createStatement().executeQuery(sqlInsert);
            while(rs.next()){
                String d1 = rs.getString(1);
                this.weightDataList.add(new WeightData(d1,rs.getDouble(2)));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        for(WeightData item : weightDataList){
            System.out.println(item);
        }

    }

    public void insertNewData(String date, Double weight){

      String sqlInsert = "INSERT INTO weightOverTime(Date,Weight) VALUES (?,?)";
      try{
            Connection conn = DbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1,date);
            stmt.setDouble(2,weight);
            stmt.execute();
            stmt.close();

      } catch(SQLException ex) {
          ex.printStackTrace();
      }


    }

    public ObservableList<WeightData> getWeightDataList() {
        return weightDataList;
    }
}
