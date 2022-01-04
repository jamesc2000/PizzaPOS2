package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.FirebaseAuthRegisterRequest;
import com.hiraya.pizzapos.httpReqRes.FirebaseAuthRegisterResponse;
import com.hiraya.pizzapos.httpReqRes.LoginRequest;
import com.hiraya.pizzapos.httpReqRes.UserFields;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AccountSettingsController implements Initializable {
    AccountSettingsModel model = new AccountSettingsModel();

    @FXML
    TextField emailField, nameField, contactField;
    @FXML
    PasswordField oldPassword, newPassword, confirmPassword;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        App.bgThreads.submit(() -> {
            this.model.getFromFirebase();
            Platform.runLater(() -> {
                this.displayData();
            });
        });
    }

    private void displayData() {
        this.emailField.setText(this.model.getEmail());
        this.contactField.setText(this.model.getContactNumber());
        this.nameField.setText(this.model.getDisplayName());
    }

    public void saveProfile() {
        this.model.setEmail(this.emailField.getText());
        this.model.setDisplayName(this.nameField.getText());
        this.model.setContactNumber(this.contactField.getText());
        // this.model.setImageUrl();
        this.model.sendToFirebase();
    }
    
    public void changePassword() {
        if (this.confirmPassword.getText().equals(this.newPassword.getText())) {
            App.bgThreads.submit(() -> {
                // Run login api to check if oldPass is correct
                try {
                    var loginRes = RestAPIHelper.login(this.model.getEmail(), this.oldPassword.getText());
                    if (loginRes.error != null) {
                        Platform.runLater(() -> {
                            Toaster.spawnToast("Error", "Invalid password", "error");
                        });
                        return;
                    }
                } catch (InterruptedException | IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
                FirebaseAuthRegisterRequest body = new FirebaseAuthRegisterRequest();
                body.idToken = App.user.getIdToken();
                body.password = this.newPassword.getText();
                try {
                    FirebaseAuthRegisterResponse res = RestAPIHelper.changePassword(body);
                    if (res.error == null) {
                        Platform.runLater(() -> {
                            Toaster.spawnToast("Password changed", "You will be logged off. Please re-login with your new password.", "success");
                        });
                        Thread.sleep(5*1000); // 5000 ms or 5 seconds
                        Platform.runLater(() -> {
                            try {
                                this.logout();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                    } else {
                        Platform.runLater(() -> {
                            Toaster.spawnToast("Error", res.error.message, "error");
                        });
                    }
                } catch (Exception e) {
                    //TODO: handle exception
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        Toaster.spawnToast("Error", e.getMessage(), "error");
                    });
                }
            });
        }

    }

    public void logout() throws IOException {
        App.user.logout();
        App.setRoot("login");
    }

    public void switchToTakeOrders() throws IOException {
        App.setRoot("takeOrders");
    }
}
