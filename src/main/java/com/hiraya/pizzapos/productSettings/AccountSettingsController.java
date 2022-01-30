package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.ImageSelector;
import com.hiraya.pizzapos.Router;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.FirebaseAuthRegisterRequest;
import com.hiraya.pizzapos.httpReqRes.FirebaseAuthRegisterResponse;
import com.hiraya.pizzapos.httpReqRes.LoginRequest;
import com.hiraya.pizzapos.httpReqRes.UserFields;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AccountSettingsController extends Router implements Initializable {
    AccountSettingsModel model = new AccountSettingsModel();
    private final URL defaultImageUrl = App.class.getResource("images/userProfilepic.png");

    @FXML
    TextField emailField, nameField, contactField;
    @FXML
    PasswordField oldPassword, newPassword, confirmPassword;
    @FXML
    ImageView userImage, profilePic;

    private String imageUrl;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        App.bgThreads.submit(() -> {
            this.model.getFromFirebase();
            Platform.runLater(() -> {
                this.displayData();
            });
        });
        this.displayImage();
        if (!App.user.profilePic.isEmpty()) {
            this.profilePic.setImage(new Image(App.user.profilePic));
            this.imageUrl = App.user.profilePic;
        }
    }

    private void displayData() {
        this.emailField.setText(this.model.getEmail());
        this.contactField.setText(this.model.getContactNumber());
        this.nameField.setText(this.model.getDisplayName());
        this.userImage.setImage(new Image(App.user.profilePic));
    }

    public void saveProfile() {
        this.model.setEmail(this.emailField.getText());
        this.model.setDisplayName(this.nameField.getText());
        this.model.setContactNumber(this.contactField.getText());
        this.model.setImageUrl(this.imageUrl);
        this.model.sendToFirebase();
        App.bgThreads.submit(() -> {
            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } // 5000 ms or 5 seconds
            Platform.runLater(() -> {
                try {
                    this.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
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
                                e.printStackTrace();
                            }
                        });
                    } else {
                        Platform.runLater(() -> {
                            Toaster.spawnToast("Error", res.error.message, "error");
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        Toaster.spawnToast("Error", e.getMessage(), "error");
                    });
                }
            });
        }

    }

    public void profileImagePopup() {
        ImageSelector<AccountSettingsController> sel = new ImageSelector<>(this);
        Stage popup = new Stage();
        popup.initOwner(App.getPrimaryStage());
        popup.setResizable(false);
        popup.initStyle(StageStyle.TRANSPARENT);

        // System.out.println(App.class.getResource("views/uploadImage.fxml").toExternalForm());
        FXMLLoader fxml = new FXMLLoader(App.class.getResource("views/uploadImage.fxml"));
        fxml.setController(sel);
        Scene scene;
        try {
            scene = new Scene(fxml.load());
            scene.setFill(Color.TRANSPARENT);
            popup.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            Toaster.spawnToast("FXML Error", e.getMessage(), "error");
        }
        popup.setOnHiding((event) -> {
            System.out.println("Closed");
            this.imageUrl = super.getImageFromPopup();
            this.displayImage();
        });
        popup.show();

    }

    private void displayImage() {
        if (this.imageUrl == null) {
            this.userImage.setImage(new Image(defaultImageUrl.toExternalForm()));
        } else {
            this.userImage.setImage(new Image(imageUrl));
        }
    }

    public void removeImage() {
        this.model.setImageUrl("");
        this.imageUrl = null;
        this.displayImage();
    }

    public void logout() throws IOException {
        App.user.logout();
        App.setRoot("login");
    }
}
