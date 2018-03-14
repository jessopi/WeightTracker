package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class WeightTrackerController {

    WeightTrackerModel weightTrackerModel = new WeightTrackerModel();

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


    public void click(ActionEvent event){
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        if(id.equals(addButton.getId())){
           if(isValidEntry()){
               System.out.println("addButton clicked");
               weightTrackerModel.insertNewData(dateEntry.getValue(),Double.parseDouble(weightEntry.getText()));
           } else {
               System.out.println("Invalid data Entry");
           }
        } else if(id.equals(searchButton.getId())){
            if(isValidSearch()){
                System.out.println("searchButton clicked");
                //pass into modelViewController for searching & updating of tabs
            } else {
                System.out.println("Invalid data Search");
            }
        }
    }

    private boolean isValidEntry(){
        if(dateEntry.getValue() == null || weightEntry.getText().isEmpty()){
            return false;
        } else if(Double.parseDouble(weightEntry.getText()) < 0){
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidSearch(){
        if(startingDateSearch.getValue() == null || endingDateSearch.getValue() == null){
            return false;
        } else if(endingDateSearch.getValue().isBefore(startingDateSearch.getValue())){
            return false;
        } else {
            return true;
        }

        //check if startingDateSearch is before ending
    }

}
