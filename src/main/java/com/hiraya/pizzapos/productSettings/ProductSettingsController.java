package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.helpers.LoadFXMLHelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class ProductSettingsController implements Initializable {
    ProductSettingsModel model = new ProductSettingsModel();
    ArrayList<ProductRowController> productControllers = new ArrayList<ProductRowController>(); // Each row in the table has their own controller

    @FXML
    VBox table;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.model.useDummyData(); // Simulate getting data from firebase
        this.displayProducts();
    }

    public void switchToAddProduct() throws IOException {
        App.setRoot("addProductsettings");
    }

    private void displayProducts() {
        FXMLLoader product;
        for (int i = 0; i < this.model.getProducts().size(); ++i) {
            try {
                product = LoadFXMLHelper.loadFXML("productSettings.product");
                this.productControllers.add(new ProductRowController(this.model.getProducts().get(i), this));
                product.setController(this.productControllers.get(i));
                this.table.getChildren().add(product.load());
            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }
        }
    }
    
}
