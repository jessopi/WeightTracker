package main.java.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.java.userutility.CurrentUser;

public class AppLoginController {

    @FXML
    private Label infoLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button createButton;

    private AppLoginModel appLoginModel = new AppLoginModel();
    public void click(javafx.event.ActionEvent event) {
       Button btn = (Button)event.getSource();
       String id = btn.getId();
       if(this.usernameField.getText().isEmpty() || this.passwordField.getText().isEmpty()){
           this.infoLabel.setText("             Empty textfield.");
           return;
       }

       if(id.equals(this.loginButton.getId())){
           if(this.appLoginModel.searchForUser(this.usernameField.getText(),this.passwordField.getText())){
               CurrentUser.setCurrentUser(this.usernameField.getText().trim().toLowerCase());
               Stage stage = (Stage)this.infoLabel.getScene().getWindow();
               stage.close();
               login();
           } else {
               infoLabel.setText("Invalid username or password.");
           }
       } else if (id.equals(this.createButton.getId())){
           if(!this.appLoginModel.searchForUser(this.usernameField.getText())){
               this.appLoginModel.createAccount(this.usernameField.getText(),this.passwordField.getText());
               infoLabel.setText("           Account created.");
           } else {
               infoLabel.setText("    Username already exists.");
           }
       }
    }

    private void login(){
        try{
            Stage weightTracker = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/main/java/application/WeightTracker.fxml"));
            Scene scene = new Scene(root);
            weightTracker.setScene(scene);
            weightTracker.setTitle("Weight Tracker Application");
            weightTracker.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}