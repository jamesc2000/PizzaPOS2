package com.hiraya.pizzapos.addProducts;

import java.net.MalformedURLException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ProductController {
    private ProductModel model = new ProductModel();

    @FXML
    private TextField name;
    @FXML
    private TextField imageUrl;
    @FXML
    private TextField category;
    @FXML
    private TextField size;
    @FXML
    private TextField price;

    public void submit() {
        try {
            this.model.setValues(
                name.getText(),
                imageUrl.getText(), 
                category.getText(), 
                size.getText(), 
                price.getText()
            );
            this.model.sendToFirestore();
        } catch (MalformedURLException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}
