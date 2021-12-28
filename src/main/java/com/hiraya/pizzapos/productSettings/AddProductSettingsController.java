package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddProductSettingsController implements Initializable {
    private Product model = new Product();
    private URL imageUrl = App.class.getResource("images/addProduct.JPG");

    @FXML
    CheckBox cb1, cb2, cb3;
    @FXML
    TextField var1, var2, var3;
    @FXML
    TextField price1, price2, price3;
    @FXML
    TextField nameField;
    @FXML
    ComboBox<String> typeField;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        // System.out.println("add products");
        this.updateCategoriesSelection();
    }
    
    public void switchToProductSettings() throws IOException {
        App.setRoot("productSettings");
    }
    
    public void onCheckboxChange(Event e) {
        CheckBox checkbox = (CheckBox)e.getSource();
        if (checkbox.getId().equals(cb1.getId())) {
            var1.setDisable(!checkbox.selectedProperty().getValue());
            price1.setDisable(!checkbox.selectedProperty().getValue());
        } else if (checkbox.getId().equals(cb2.getId())) {
            var2.setDisable(!checkbox.selectedProperty().getValue());
            price2.setDisable(!checkbox.selectedProperty().getValue());
        } else if (checkbox.getId().equals(cb3.getId())) {
            var3.setDisable(!checkbox.selectedProperty().getValue());
            price3.setDisable(!checkbox.selectedProperty().getValue());
        }
    }

    public void submit() throws IOException {
        ArrayList<String> sizes = new ArrayList<String>(3);
        ArrayList<Double> prices = new ArrayList<Double>(3);
        TextField[] sizeFields = {var1, var2, var3};
        TextField[] priceFields = {price1, price2, price3};
        for (int i = 0; i < 3; ++i) {
            if (sizeFields[i].getText().isEmpty()) {
                sizes.add(null);
            } else {
                sizes.add(sizeFields[i].getText());
            }

            if (priceFields[i].getText().isEmpty()) {
                prices.add(0.0);
            } else {
                prices.add(Double.valueOf(priceFields[i].getText()));
            }
        }
        try {
            this.model.setValues(
                nameField.getText(),
                this.imageUrl.toExternalForm(),
                typeField.getValue().toString(), 
                // sizes, // TODO: Edit addProductFields to have an array value for these
                // prices
                "smol",
                "10.0"
            );

            Future<String> send = App.bgThreads.submit(() -> {
                try {
                    this.model.sendToFirestore();
                    Platform.runLater(() -> {
                        Toaster.spawnToast("Succes", "Created " + this.model.getName(), "success");
                    });
                } catch (Exception e) {
                    //TODO: handle exception
                    Platform.runLater(() -> {
                        Toaster.spawnToast("Error in creating new product.", e.getCause().toString(), "Error");
                    });
                    e.printStackTrace();
                }
                return "Done";
            });
        } catch (MalformedURLException e) {
            //TODO: handle exception
            Toaster.spawnToast("Invalid image URL.", this.imageUrl.toExternalForm() + " is not a valid URL.", "error");
            e.printStackTrace();
        }
    }

    public void addProductPopup() {
        Stage popup = new Stage();
        popup.initOwner(App.getPrimaryStage());
        popup.setResizable(false);
        popup.initStyle(StageStyle.TRANSPARENT);

        FXMLLoader fxml = new FXMLLoader(App.class.getResource("views/newCategory.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxml.load());
            scene.setFill(Color.TRANSPARENT);
            popup.setScene(scene);
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
            Toaster.spawnToast("FXML Error", e.getMessage(), "error");
        }
        popup.setOnHiding((event) -> {
            System.out.println("this ran");
            this.updateCategoriesSelection();
        });
        popup.show();
    }

    public void updateCategoriesSelection() {
        ArrayList<Category> categories = new ArrayList<Category>();
        try {
            categories = RestAPIHelper.getCategories(App.user.getIdToken());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Toaster.spawnToast("Error fetching categories", e.getLocalizedMessage(), "error");
        }
        typeField.getItems().clear();
        categories.forEach(category -> {
            typeField.getItems().add(category.name);
        });
    }
}