package com.hiraya.pizzapos;

import java.io.IOException;

import com.hiraya.pizzapos.productSettings.Product;

public class Router {
    String popupImage;
    public void switchToTransactionHistory() {
        try {
            App.setRoot("transactionHistory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToProductSettings() {
        try {
            App.setRoot("productSettings");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void switchToTakeOrders() {
        try {
            App.setRoot("takeOrders");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void switchToAccountSettings() {
        try {
            App.setRoot("accountSettings");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void switchToAddProduct() {
        try {
            App.setRoot("addProductsettings");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setImageFromPopup(String link) {
        this.popupImage = link;
    }

    public String getImageFromPopup() {
        return this.popupImage;
    }
}
