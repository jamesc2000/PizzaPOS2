package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;

import javafx.fxml.Initializable;

public class ProductSettingsController implements Initializable {

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }

    public void switchToAddProduct() throws IOException {
        App.setRoot("addProductsettings");
    }
    
}
