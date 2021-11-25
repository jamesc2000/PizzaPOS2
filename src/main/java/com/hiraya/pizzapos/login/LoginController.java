package com.hiraya.pizzapos.login;

import com.hiraya.pizzapos.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    // Declare the model
    LoginModel model = new LoginModel();
    
    // Declare the elements with ids here so we can control them
    
    @FXML
    private TextField emailField;  // See the FXML file in scene builder and then on the
    @FXML                      // right side under Code, we can see that the ids for
    private TextField passwordField; // these fields are "emailField" and "passwordField"
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Initialize");
    }

    @FXML
    public void login() {
        System.out.println("Login clicked");
        
        // When login is clicked, store the data to the model
        model.setEmail(emailField.getText());
        model.setPassword(passwordField.getText());

        // Send the email and password to firebase, then firebase is
        // responsible for verifying credentials, not us
        model.sendToFirebase();
    }
    
    @FXML
    public void switchToApp() throws IOException {
        App.setRoot("App");
    }
    
    @FXML
    public void changeViewToRegister() throws IOException {
        App.setRoot("register");
    }
}
