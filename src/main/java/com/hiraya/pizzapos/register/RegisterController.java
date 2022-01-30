package com.hiraya.pizzapos.register;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.ImageSelector;
import com.hiraya.pizzapos.Router;
import com.hiraya.pizzapos.Toaster;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterController extends Router implements Initializable {
    private final URL defaultImageUrl = App.class.getResource("images/userProfilepic.png");
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
    private ImageView userImage;

    private String imageUrl;
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
        if (!this.passwordField.getText().equals(this.confirmPasswordField.getText())) {
            Toaster.spawnToast("Invalid input", "Passwords do not match", "error");
            return;
        }
        System.out.println("Submit clicked");

        // When login is clicked, store the data to the model
        model.setEmail(emailField.getText());
        model.setPassword(passwordField.getText());
        model.setLastName(lastName.getText());
        model.setFirstName(firstName.getText());
        model.setImageUrl(this.imageUrl);
        // TODO: Use this pag kumpleto na yung fields sa ui, temp lang yung nasa taas
//        model.setFields(email, password, firstName, lastName);

        // Send the email and password to firebase, then firebase is
        // responsible for creating the account on our app 
        if (model.sendToFirebase().isSuccessful()) {
            App.setRoot("login");
        } else {
            Toaster.spawnToast("Error in Creating Account", "", "error");
        }
    }

    @FXML
    public void changeViewToLogin() throws IOException {
        App.setRoot("login");
    }
    
    public void setImagePopup() {
        ImageSelector<RegisterController> sel = new ImageSelector<>(this);
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        App.getPrimaryStage().setWidth(512.0);
        App.getPrimaryStage().setHeight(700.0);
    }
}