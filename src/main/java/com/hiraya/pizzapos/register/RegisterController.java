package com.hiraya.pizzapos.register;

import java.io.IOException;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.CONSTANTS;
import com.hiraya.pizzapos.Toaster;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterController {
    RegisterModel model = new RegisterModel();

    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private TextField lastName;
    @FXML
    private TextField firstName;
    @FXML
    public boolean checkConfirmIfSame() {
        if (passwordField.getText().equals(confirmPasswordField.getText())) {
            System.out.println("Passwords are the same");
            return true;
        } else {
            System.out.println("Passwords are not the same");
            return false;
        }
    }
    
    @FXML
    public void submit() throws IOException {
        System.out.println("Submit clicked");

        // When login is clicked, store the data to the model
        model.setEmail(emailField.getText());
        model.setPassword(passwordField.getText());
        model.setLastName(lastName.getText());
        model.setFirstName(firstName.getText());
        // TODO: Use this pag kumpleto na yung fields sa ui, temp lang yung nasa taas
//        model.setFields(email, password, firstName, lastName);

        // Send the email and password to firebase, then firebase is
        // responsible for creating the account on our app 
        if (model.sendToFirebase().isSuccessful()) {
            App.setRoot("login");
        } else {
            Toaster.spawnToast(App.getPrimaryStage(), "Error in Creating Account", "", CONSTANTS.toastDelay, CONSTANTS.fadeInDelay, CONSTANTS.fadeOutDelay);
        }
    }

    @FXML
    public void changeViewToLogin() throws IOException {
        App.setRoot("login");
    }
}