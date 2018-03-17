package Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Label infoLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private LoginModel loginModel = new LoginModel();

    public void click(javafx.event.ActionEvent event) {
       Button btn = (Button)event.getSource();
       String id = btn.getId();
       if(this.usernameField.getText().isEmpty() || this.passwordField.getText().isEmpty()){
           this.infoLabel.setText("             Empty textfield.");
           return;
       }

       if(id.equals("loginButton")){
           if(this.loginModel.searchForUser(this.usernameField.getText(),this.passwordField.getText())){
               Stage stage = (Stage)this.infoLabel.getScene().getWindow();
               stage.close();
               login();
           } else {
               infoLabel.setText("Invalid username or password.");
           }
       } else if (id.equals("createButton")){
           if(!this.loginModel.searchForUser(this.usernameField.getText())){
               this.loginModel.createAccount(this.usernameField.getText(),this.passwordField.getText());
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
            Pane root = loader.load(getClass().getResource("/Main/WeightTracker.fxml"));
            root.setUserData(this.usernameField.getText());
            Scene scene = new Scene(root);
            weightTracker.setScene(scene);
            weightTracker.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
