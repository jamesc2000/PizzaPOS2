package com.hiraya.pizzapos.productSettings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Future;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.NewCategoryFields;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class NewCategoryController implements Initializable {
    @FXML
    Button close;
    @FXML
    ImageView imgPreview;
    @FXML
    TextField name, image;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        image.textProperty().addListener((observable, oldVal, newVal) -> {
            this.imagePreview();
        });
    }

    public void imagePreview() {
        try {
            if (this.image.getText().isEmpty()) {
                imgPreview.setImage(new Image(App.class.getResource("images/addProduct.JPG").toExternalForm()));
            } else {
                imgPreview.setImage(new Image(new URL(this.image.getText()).toExternalForm()));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toaster.spawnToast("Invalid URL", "", "error");
        }
    }
    
    public void submit() {
        NewCategoryFields data = new NewCategoryFields(name.getText(), image.getText());

        Future<String> task = App.bgThreads.submit(() -> {
            try {
                RestAPIHelper.createCategory(data, App.user.getIdToken());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Toaster.spawnToast("Error in creating " + this.name.getText(), e.getMessage(), "error");
                    this.close();
                });
            }
            Platform.runLater(() -> {
                Toaster.spawnToast("Created category", this.name.getText(), "succes");
                this.close();
            });
            return "Done";
        });
    }

    public void close() {
        Stage popup = (Stage) this.close.getScene().getWindow();
        popup.close();
    }
}
