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
import com.hiraya.pizzapos.ImageSelector;
import com.hiraya.pizzapos.Router;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddProductSettingsController extends Router implements Initializable {
    private Product model = new Product();
    private final URL defaultImageUrl = App.class.getResource("images/addProduct.JPG");
    private String imageUrl;

    @FXML
    CheckBox cb1, cb2, cb3;
    @FXML
    TextField var1, var2, var3;
    @FXML
    TextField price1, price2, price3;
    @FXML
    TextField nameField;;
    @FXML
    ImageView productImage;
    @FXML
    ComboBox<String> typeField;
    @FXML
    ImageView profilePic;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        // System.out.println("add products");
        this.updateCategoriesSelection();
        this.displayImage();
        this.profilePic.setImage(new Image(App.user.profilePic));
    }

    public AddProductSettingsController() {

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
        if (this.nameField.getText() == null) {
            Toaster.spawnToast("Name is required", "", "error");
        }
        if (this.typeField.getValue() == null) {
            Toaster.spawnToast("Category is required", "", "error");
        }
        if (this.imageUrl == null) {
            this.imageUrl = "";
        }
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
        this.model.setValues(
            "temp",
            nameField.getText(),
            this.imageUrl,
            typeField.getValue().toString(), 
            sizes,
            prices
        );

        Future<String> send = App.bgThreads.submit(() -> {
            try {
                this.model.sendToFirestore();
                Platform.runLater(() -> {
                    Toaster.spawnToast("Success", "Created " + this.model.getName(), "success");
                    super.switchToProductSettings();
                });
            } catch (Exception e) {
                //TODO: handle exception
                Platform.runLater(() -> {
                    Toaster.spawnToast("Error in creating new product.", e.getMessage().toString(), "Error");
                });
                e.printStackTrace();
            }
            return "Done";
        });
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

    public void displayImage() {
        if (this.imageUrl == null) {
            this.productImage.setImage(new Image(defaultImageUrl.toExternalForm()));
        } else {
            this.productImage.setImage(new Image(imageUrl));
        }
    }

    public void setImagePopup() {
        ImageSelector<AddProductSettingsController> sel = new ImageSelector<>(this);
        Stage popup = new Stage();
        popup.initOwner(App.getPrimaryStage());
        popup.setResizable(false);
        popup.initStyle(StageStyle.TRANSPARENT);

        // System.out.println(App.class.getResource("views/uploadImage.fxml").toExternalForm());
        FXMLLoader fxml = new FXMLLoader(App.class.getResource("views/uploadImage.fxml"));
        fxml.setController(sel);
        Scene scene;
        try {
            scene = new Scene(fxml.load());
            scene.setFill(Color.TRANSPARENT);
            popup.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            Toaster.spawnToast("FXML Error", e.getMessage(), "error");
        }
        popup.setOnHiding((event) -> {
            System.out.println("Closed");
            this.imageUrl = super.getImageFromPopup();
            this.displayImage();
        });
        popup.show();
    }
}