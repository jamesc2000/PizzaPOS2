package com.hiraya.pizzapos.productSettings;

import java.net.URL;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;

import javafx.application.Platform;

public class Category {
    public String id;
    public String name;
    public URL imageUrl;

    public void deleteCategory() {
        App.bgThreads.submit(() -> {
            try {
                RestAPIHelper.deleteCategory(id, App.user.getIdToken());
                Platform.runLater(() -> {
                    Toaster.spawnToast("Deleted " + this.name, "", "success");
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Platform.runLater(() -> {
                    Toaster.spawnToast("Error deleting category" + e.getMessage(), "", "error");
                });
            }
        });
    }
}
