package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class WeightTrackerController {

    private WeightTrackerModel weightTrackerModel = new WeightTrackerModel();

    @FXML
    private TextField weightEntry;
    @FXML
    private DatePicker dateEntry;
    @FXML
    private Button addButton;
    @FXML
    private Button searchButton;
    @FXML
    private DatePicker startingDateSearch;
    @FXML
    private DatePicker endingDateSearch;
    @FXML
    private TableView<WeightData> dataTable;
    @FXML
    private TableColumn<WeightData,String> dateCol;
    @FXML
    private TableColumn<WeightData,Double> weightCol;
    @FXML
    private LineChart<?,?> weightChart;


    /*
            things to do:
                seperate code logic a bit more
                Test linegraph with 100s of data entries to make sure it works properly (JUNIT)
                Add labels to X & Y axis
                add functionality to load all data with click of button
                ability to delete entries/remove all?
                Add information on additional tab - info about current set of data
                have a status bar representing current databse connection.

     */
    public void click(ActionEvent event){
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        if(id.equals(addButton.getId())){
           if(isValidEntry()){
               System.out.println("addButton clicked");
               weightTrackerModel.insertNewData(dateEntry.getValue().toString(),Double.parseDouble(weightEntry.getText()));
           } else {
               System.out.println("Invalid data Entry");
           }
        } else if(id.equals(searchButton.getId())){
            if(isValidSearch()){
                System.out.println("searchButton clicked");
                updateTable();
                updateLineChart();
            } else {
                System.out.println("Invalid data Search");
            }
        }
    }

    private void updateTable(){
        this.weightTrackerModel.setSearchRange(this.startingDateSearch.getValue().toString(),this.endingDateSearch.getValue().toString());
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        this.weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.dataTable.setItems(this.weightTrackerModel.getWeightDataList());
    }
    private void updateLineChart(){
        this.weightChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        NumberAxis yAxis = (NumberAxis)this.weightChart.getYAxis();
        yAxis.setForceZeroInRange(false);
        for(WeightData item : this.weightTrackerModel.getWeightDataList()){
            series.getData().add(new XYChart.Data(item.getDate(),item.getWeight()));
        }
        this.weightChart.getData().add(series);
    }
    private boolean isValidSearch(){
        try{
            if(startingDateSearch.getValue() == null || endingDateSearch.getValue() == null ||endingDateSearch.getValue().isBefore(startingDateSearch.getValue())){
                throw new IllegalArgumentException("Illegal values entered");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("CAUGHT");
            return false;
        }
        return true;
    }
    private boolean isValidEntry(){
        try {
            if(Double.parseDouble(weightEntry.getText()) < 0 ||  dateEntry.getValue() == null){
                throw new IllegalArgumentException("Illegal values entered");
            }
        } catch(IllegalArgumentException ex) {
            return false;
        }
        return true;
    }
}
