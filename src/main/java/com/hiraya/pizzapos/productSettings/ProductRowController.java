package com.hiraya.pizzapos.productSettings;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProductRowController implements Initializable {
    final private ProductSettingsController parent;
    final private Product model;

    @FXML
    ImageView img;
    @FXML
    VBox sizesContainer;
    @FXML
    Text name;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        System.out.println("Controller injection success. Parent: " + this.parent);
        this.displayData();
    }

    public ProductRowController(Product model, ProductSettingsController parent) {
        this.model = model;
        this.parent = parent;
    }
    
    private void displayData() {
        this.img.setImage(new Image(this.model.getImage().toExternalForm()));
        this.name.setText(this.model.getName());

        this.model.getSizesArr().forEach(size -> {
            Text sizeTxt = new Text(size);
            sizeTxt.getStyleClass().add("rowText");
            sizesContainer.getChildren().add(sizeTxt);
        });
    }
}
