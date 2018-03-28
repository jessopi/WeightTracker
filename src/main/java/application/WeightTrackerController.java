package main.java.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.weightcontainer.WeightData;

/*
    Controller class for the main weightTracker Application
    Handles events when buttons are clicked.
    Such as search,remove, and load all.

*/
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
    @FXML
    private ListView additionaldata;

    private WeightTrackerModel weightTrackerModel = new WeightTrackerModel();

    //Click event function
    public void click(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();

        clearButtonErrors();
        if (id.equals(addButton.getId())) {
            if (isValidEntry()) {
                weightTrackerModel.insertNewData(dateEntry.getValue().toString(), Double.parseDouble(weightEntry.getText()));
            }
        } else if (id.equals(searchButton.getId())) {
            if (isValidSearch()) {
                this.weightTrackerModel.setSearchRange(this.startingDateSearch.getValue().toString(),this.endingDateSearch.getValue().toString());
                updateTable();
                updateLineChart();
                updateAdditionalInfo();
            }
        } else if (id.equals(removeByDateButton.getId())) {
            if(isValidRemoval()){
                this.weightTrackerModel.removeData(dateRemovalField.getValue().toString());
            }
        } else if (id.equals(loadButton.getId())) {
            this.weightTrackerModel.setSearchRange("","");
            updateTable();
            updateLineChart();
            updateAdditionalInfo();
        } else if (id.equals(removeAllButton.getId())){
            //questionable if this should be added
            this.weightTrackerModel.removeData("");
        }
    }

    //updates the data used table when a new search range is entered
    private void updateTable(){
        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        this.weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.dataTable.setItems(this.weightTrackerModel.getWeightDataList());
    }

    //updates the line graph when a new search range is entered
    private void updateLineChart(){
        this.weightChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        for(WeightData item : this.weightTrackerModel.getWeightDataList()){
            series.getData().add(new XYChart.Data(item.getDate(),item.getWeight()));
        }
        this.weightChart.getData().add(series);
    }

    //Checks if datepicker boxes are not null and that the starting date is before the ending date
    private boolean isValidSearch(){
        if(startingDateSearch.getValue() == null || endingDateSearch.getValue() == null || endingDateSearch.getValue().isBefore(startingDateSearch.getValue())){
            searchFieldError.setText("Invalid Search Entry");
            return false;
        } else {
            searchFieldError.setText("");
            return true;
        }
    }

    //Checks if textfields are null or a nonvalid argument.
    private boolean isValidEntry(){

        try {
            if(Double.parseDouble(weightEntry.getText()) < 0 ||  dateEntry.getValue() == null){
                throw new IllegalArgumentException("Invalid Input Entry");
            }
        } catch(IllegalArgumentException ex) {
            entryFieldError.setText("Invalid Input Entry");
            return false;
        }
        entryFieldError.setText("");
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

    //updates the 3rd tab that contains additional info on the search range.
    private void updateAdditionalInfo(){
        additionaldata.setStyle("-fx-font-size: 1.8em ;");
        ObservableList<String> thing = FXCollections.observableList(weightTrackerModel.getAdditionalInfo());
        additionaldata.setItems(thing);
    }

    //clears button label errors
    private void clearButtonErrors(){
        entryFieldError.setText("");
        removalFieldError.setText("");
        searchFieldError.setText("");
    }
}
