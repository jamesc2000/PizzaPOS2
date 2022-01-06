package com.hiraya.pizzapos.login;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Future;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.LoginResponse;
import com.hiraya.pizzapos.httpReqRes.SendRefreshTokenRequest;
import com.hiraya.pizzapos.httpReqRes.SendRefreshTokenResponse;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    // Declare the model
    LoginModel model = new LoginModel();
    
    // Declare the elements with ids here so we can control them
    
    @FXML
    private TextField emailField;  // See the FXML file in scene builder and then on the
    @FXML                      // right side under Code, we can see that the ids for
    private TextField passwordField; // these fields are "emailField" and "passwordField"
    @FXML
    private ProgressIndicator loadingSpinner;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize");
    }

    public void test() {
        System.out.println("controller working");
    }

    @FXML
    public void login() throws IOException, InterruptedException {
        loadingSpinner.setVisible(true);
        // When login is clicked, store the data to the model
        model.setEmail(emailField.getText());
        model.setPassword(passwordField.getText());

        // Send the email and password asynchronously to firebase, then firebase is
        // responsible for verifying credentials, not us
        Future<String> task = App.bgThreads.submit(() -> {
            LoginResponse response = model.sendToFirebase();
            if (Objects.isNull(response.error)) {
                // If no errors, proceed with handshake, exchange login refreshToken
                // for accessTokens and idTokens
                SendRefreshTokenRequest request = new SendRefreshTokenRequest();

                request.refresh_token = response.refreshToken;
                SendRefreshTokenResponse finalAuthData = RestAPIHelper.sendRToken(request);

                // After the handshake is complete, put the tokens to the actual User
                if (!Objects.isNull(finalAuthData.access_token) && !Objects.isNull(finalAuthData.refresh_token)) {
                    Platform.runLater(() -> {
                        App.user.setTokens(
                            finalAuthData.refresh_token,
                            finalAuthData.id_token,
                            finalAuthData.access_token,
                            response.localId
                        );
                        try {
                            var userData = RestAPIHelper.getUserData();
                            App.user.profilePic = userData.imageUrl.stringValue;
                        } catch (IOException | InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Log in successful");
                        // Display toast notif here saying "Log in successful"
                        Toaster.spawnToast("Login successful", "Welcome to PizzaPOS", "success");
                        try {
                            App.setRoot("takeOrders");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                
            } else {
                // If there are errors, show a notification to the user containing the message
                // Common error codes for firebase auth API found here:
                // https://firebase.google.com/docs/reference/rest/auth/#section-sign-in-email-password
                Platform.runLater(() -> {
                    if (response.error.message.equals("EMAIL_NOT_FOUND")) {
                        System.out.println("Email not registered!");
                        Toaster.spawnToast("Email not registered!", "Please, try again!", "error");
                        // Display toast notif here saying "Email not registered"
                    } else if (response.error.message.equals("INVALID_PASSWORD")) {
                        System.out.println("Invalid password!");
                        Toaster.spawnToast("Invalid Password!", "Please, try again!", "error");
                        // Same thing for notifs here
                    } else if (response.error.message.equals("USER_DISABLED")) {
                        System.out.println("User has been disabled by administrator.");
                        Toaster.spawnToast("User has been disabled.", "Please, try again!", "error");
                        // Same thing for notifs here
                    } else {
                        // If the error message from API is unrecognized, just display it
                        // with the ugly format as is
                        System.out.println(response.error.message);
                        Toaster.spawnToast("ERROR", response.error.message, "error");
                        // Display a notif still so user can be aware
                    }
                });
            }
            Platform.runLater(() -> {
                loadingSpinner.setVisible(false);
            });
            return "Done";
        });
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
