package com.hiraya.pizzapos.productSettings;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProductRowController implements Initializable {
    final private DecimalFormat formatter = new DecimalFormat("₱ #0.00");
    final private ProductSettingsController parent;
    private Product model;

    @FXML
    ImageView img;
    @FXML
    VBox sizesContainer, pricesContainer;
    @FXML
    Text name;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        System.out.println("Controller injection success. Parent: " + this.parent);
        this.displayData();
    }

    public ProductRowController(Product model, ProductSettingsController parent) {
        this.model = model;
        this.parent = parent;
    }
    
    private void displayData() {
        if (this.model.getImage() != null) {
            this.img.setImage(new Image(this.model.getImage().toExternalForm()));
        } else {
            this.img.setImage(new Image(App.class.getResource("images/addProduct.JPG").toExternalForm()));
        }
        this.name.setText(this.model.getName());

        this.model.getSizesArr().forEach(size -> {
            Text sizeTxt = new Text(size);
            sizeTxt.getStyleClass().add("rowText");
            sizesContainer.getChildren().add(sizeTxt);
        });

        this.model.getPricesArr().forEach(price -> {
            Text priceTxt = new Text(formatter.format(price));
            priceTxt.getStyleClass().add("rowText");
            pricesContainer.getChildren().add(priceTxt);
        });
    }

    public void deleteProduct() {
        App.bgThreads.submit(() -> {
            try {
                RestAPIHelper.deleteProduct(this.model.documentId, App.user.getIdToken());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Platform.runLater(() -> {
                    e.printStackTrace();
                    Toaster.spawnToast(e.getLocalizedMessage(), "", "error");
                    this.parent.displayProducts();
                });
            }
        });
        this.parent.model.getProducts().remove(this.model);
        System.out.println("Removed: " + this);
        System.out.println("Remaining in parent model: ");
        this.parent.model.getProducts().forEach(pr -> {
            System.out.println(pr);
        });
        this.parent.displayProducts();
    }

    public void editProduct() {

    }
}
