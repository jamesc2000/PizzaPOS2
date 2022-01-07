package com.hiraya.pizzapos;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ImageSelector<T extends Router> implements Initializable {
    T parentController;

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

    public ImageSelector(T parent) {
        this.parentController = parent;
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
        String link = this.image.getText();
        parentController.setImageFromPopup(link);
        this.close();
    }

    public void close() {
        Stage me = (Stage) this.image.getScene().getWindow();
        me.close();
    }
}
