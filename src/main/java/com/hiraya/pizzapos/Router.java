package com.hiraya.pizzapos;

import java.io.IOException;

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
            e.printStackTrace();
        }
    }

    public void switchToTakeOrders() {
        try {
            App.setRoot("takeOrders");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAccountSettings() {
        try {
            App.setRoot("accountSettings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAddProduct() {
        try {
            App.setRoot("addProductsettings");
        } catch (IOException e) {
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
