package com.hiraya.pizzapos.takeOrders;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.addProducts.ProductModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class MenuOrderController implements Initializable {
    private ProductModel data;

    @FXML
    private Label name;
    private String tempName;

    @FXML
    private ImageView image;

    @FXML
    private GridPane productContainer;

    @FXML
    private GridPane sizesContainer;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("2222");
        this.name.setText(this.data.getName());
        this.image.setImage(new Image(this.data.getImage().toExternalForm()));
        this.mapSizes();
    }

    public MenuOrderController(ProductModel product) {
        this.data = product;
    }

    private void mapSizes() {
        for (int i = 0; i < data.getSizesArr().size(); ++i) {
            Button btn = new Button(data.getSizesArr().get(i));
            btn.setOnAction(e -> {
                System.out.println(e);
                System.out.println(e.getSource());
            });
            sizesContainer.add(btn, i + 1, 0);
            GridPane.setConstraints(btn, i + 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        }
    }
}
