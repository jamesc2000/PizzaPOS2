package com.hiraya.pizzapos.register;

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
    public void submit() {
        System.out.println("Submit clicked");

        // When login is clicked, store the data to the model
        model.setEmail(emailField.getText());
        model.setPassword(passwordField.getText());

        // TODO: Use this pag kumpleto na yung fields sa ui, temp lang yung nasa taas
//        model.setFields(email, password, firstName, lastName);

        // Send the email and password to firebase, then firebase is
        // responsible for creating the account on our app 
        model.sendToFirebase();
    }

}
