package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
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

    private Boolean isEdit = false; // edit flag

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
    @FXML
    Button saveBtn, cancelBtn;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.updateCategoriesSelection();
        this.displayImage();
        if (!App.user.profilePic.isEmpty()) {
            this.profilePic.setImage(new Image(App.user.profilePic));
        }
    }

    public void setPreFilledData(Product pr) {
        if (pr == null) return;// Guard clause
        // System.out.println("Controller working " + pr.getName());
        this.isEdit = true;
        this.model = pr;
        this.updateCategoriesSelection();
        if (pr.getImage() != null) {
            this.imageUrl = pr.getImage().toExternalForm();
        }
        this.displayImage();
        this.nameField.setText(pr.getName());
        this.typeField.setValue(pr.getCategory());
        
        CheckBox[] cbs = { cb1, cb2, cb3 };
        TextField[] vars = { var1, var2, var3 };
        TextField[] prices = { price1, price2, price3 };
        for (int sizeIdx = 0; sizeIdx < pr.getSizesArr().size(); sizeIdx++) {
            if (pr.getSizesArr().get(sizeIdx) != null) {
                cbs[sizeIdx].setSelected(true);
                vars[sizeIdx].setDisable(false);
                prices[sizeIdx].setDisable(false);
                vars[sizeIdx].setText(pr.getSizesArr().get(sizeIdx));
            } else {
                vars[sizeIdx].setDisable(true);
                prices[sizeIdx].setDisable(true);
                cbs[sizeIdx].setSelected(false);
                vars[sizeIdx].clear();
            }

            if (pr.getPricesArr().get(sizeIdx) != null) {
                prices[sizeIdx].setText(pr.getPricesArr().get(sizeIdx).toString());
            } else {
                prices[sizeIdx].clear();
            }
        }

        // IMPORTANT, change the onAction of the save button from submit to edit
        // saveBtn.setOnAction((event) -> {
        //     System.out.println(pr.getDocumentId());
        //     System.out.println("Overridden saveBtn " + this.imageUrl);
        //     this.submit();
        //     // this.model.setImage(this.imageUrl);
        //     // App.bgThreads.submit(() -> {
        //     //     this.model.patchFirestore();
        //     //     Platform.runLater(() -> {
        //     //         Toaster.spawnToast("Edited product", "Successfully edited " + this.model.getName(), "success");
        //     //         super.switchToProductSettings();
        //     //     });
        //     // });
        // });
        cancelBtn.setOnAction((event) -> {
            super.switchToProductSettings();
        });
    }

    public void removeImage() {
        this.model.setImage("");
        this.imageUrl = null;
        this.displayImage();
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
        if (this.nameField.getText() == "") {
            Toaster.spawnToast("Name is required", "", "error");
        }
        else if (this.typeField.getValue() == null) {
            Toaster.spawnToast("Category is required", "", "error");
        }
        else if (this.cb1.isSelected() == false && this.cb2.isSelected()== false && this.cb3.isSelected()== false){
            Toaster.spawnToast ("1 Variation required", "", "error");
            return;
        }
        else if (this.cb1.isSelected() == true && this.var1.getText() == "" || this.price1.getText() == ""){
            Toaster.spawnToast ("Parameters required", "", "error");
            return;
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

        if (!this.isEdit) {
            this.model.setValues(
                "temp",
                nameField.getText(),
                this.imageUrl,
                typeField.getValue().toString(), 
                sizes,
                prices
            );
        } else {
            this.model.setValues(
                this.model.getDocumentId(), 
                nameField.getText(), 
                this.imageUrl, 
                typeField.getValue().toString(), 
                sizes, 
                prices
            );
        }

        Future<String> send = App.bgThreads.submit(() -> {
            try {
                if (!this.isEdit) {
                    this.model.sendToFirestore();
                    Platform.runLater(() -> {
                        Toaster.spawnToast("Success", "Created " + this.model.getName(), "success");
                        super.switchToProductSettings();
                    });
                } else {
                    this.model.patchFirestore();
                    Platform.runLater(() -> {
                        Toaster.spawnToast("Success", "Edited " + this.model.getName(), "success");
                        super.switchToProductSettings();
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Toaster.spawnToast("Error in saving product.", e.getMessage().toString(), "Error");
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