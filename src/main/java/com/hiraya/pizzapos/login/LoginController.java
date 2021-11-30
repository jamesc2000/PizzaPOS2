package com.hiraya.pizzapos.login;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.httpReqRes.LoginResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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
    public void login() throws IOException {
        System.out.println("Login clicked");
        
        // When login is clicked, store the data to the model
        model.setEmail(emailField.getText());
        model.setPassword(passwordField.getText());

        // Send the email and password to firebase, then firebase is
        // responsible for verifying credentials, not us
        LoginResponse response = model.sendToFirebase();
        if (Objects.isNull(response.error)) {
            // If no errors, proceed with handshake, exchange login refreshToken
            // for accessTokens and idTokens
            
            // Boss CJ insert mo dito code for the handshake hehe

            // After the handshake is complete, put the tokens to the actual User
            // Uncomment line below when handshake logic is complete
            //App.user.setTokens(refreshToken, idToken, accessToken);
            
            System.out.println("Log in successful");
            // Display toast notif here saying "Log in successful"
            App.setRoot("takeOrders");
        } else {
            // If there are errors, show a notification to the user containing the message
            // Common error codes for firebase auth API found here:
            // https://firebase.google.com/docs/reference/rest/auth/#section-sign-in-email-password
            if (response.error.message.equals("EMAIL_NOT_FOUND")) {
                System.out.println("Email not registered!");
                // Display toast notif saying "Email not registered"
                // TODO: Make a wrapper function to spawn toast notifications in UI
            } else if (response.error.message.equals("INVALID_PASSWORD")) {
                System.out.println("Invalid password!");
                // Same thing for notifs here
            } else if (response.error.message.equals("USER_DISABLED")) {
                System.out.println("User has been disabled by administrator.");
                // Same thing for notifs here
            } else {
                // If the error message from API is unrecognized, just display it
                // with the ugly format as is
                System.out.println(response.error.message);
                // Display a notif still so user can be aware
            }
        }
    }
    
    @FXML
    public void switchToApp() throws IOException {
        App.setRoot("App");
    }
    
    @FXML
    public void changeViewToRegister() throws IOException {
        App.setRoot("registerSample");
    }
}
