package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class WeightTrackerController {


    @FXML
    private TextField weightEntry;
    @FXML
    private Label entryFieldError;
    @FXML
    private Label searchFieldError;
    @FXML
    private Label removalFieldError;
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
    @FXML
    private DatePicker dateRemovalField;
    @FXML
    private Button loadButton;
    @FXML
    private Button removeAllButton;
    @FXML
    private Button removeByDateButton;

    private WeightTrackerModel weightTrackerModel = new WeightTrackerModel();
    /*
            things to do:
                Add information on additional tab - info about current set of data
                class to calculate data
     */

    public void click(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();


        if (id.equals(addButton.getId())) {
            if (isValidEntry()) {
                weightTrackerModel.insertNewData(dateEntry.getValue().toString(), Double.parseDouble(weightEntry.getText()));
            }
        } else if (id.equals(searchButton.getId())) {
            if (isValidSearch()) {
                this.weightTrackerModel.setSearchRange(this.startingDateSearch.getValue().toString(),this.endingDateSearch.getValue().toString());
                updateTable();
                updateLineChart();
            }
        } else if (id.equals(removeByDateButton.getId())) {
            if(isValidRemoval()){
                this.weightTrackerModel.removeData(dateRemovalField.getValue().toString());
            }
        } else if (id.equals(loadButton.getId())) {
            this.weightTrackerModel.setSearchRange("","");
            updateTable();
            updateLineChart();
        } else if (id.equals(removeAllButton.getId())){
            //questionable if this should be added
            this.weightTrackerModel.removeData("");
        }
    }

    private void updateTable(){
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        this.weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.dataTable.setItems(this.weightTrackerModel.getWeightDataList());
    }
    private void updateLineChart(){
        this.weightChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        for(WeightData item : this.weightTrackerModel.getWeightDataList()){
            series.getData().add(new XYChart.Data(item.getDate(),item.getWeight()));
        }
        this.weightChart.getData().add(series);
    }

    private boolean isValidSearch(){
        if(startingDateSearch.getValue() == null || endingDateSearch.getValue() == null ||endingDateSearch.getValue().isBefore(startingDateSearch.getValue())){
            searchFieldError.setText("Invalid Search Entry");
            return false;
        } else {
            searchFieldError.setText("");
            return true;
        }
    }
    private boolean isValidEntry(){

        try {
            if(Double.parseDouble(weightEntry.getText()) < 0 ||  dateEntry.getValue() == null){
                throw new IllegalArgumentException("Invalid Input Entry");
            }
        } catch(IllegalArgumentException ex) {
            entryFieldError.setText("Invalid Input Entry");
            return false;
        }
        searchFieldError.setText("");
        return true;
    }
    private boolean isValidRemoval(){
        if(dateRemovalField.getValue() == null){
            removalFieldError.setText("Invalid Date Entry");
            return false;
        } else {
            removalFieldError.setText("");
            return true;
        }
    }
}
