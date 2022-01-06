package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Router;
import com.hiraya.pizzapos.helpers.LoadFXMLHelper;
import com.hiraya.pizzapos.helpers.RestAPIHelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductSettingsController extends Router implements Initializable {
    ProductSettingsModel model = new ProductSettingsModel();
    ArrayList<ProductRowController> productControllers = new ArrayList<ProductRowController>(); // Each row in the table has their own controller

    private String currentCategory = "All";

    @FXML
    VBox table;
    @FXML
    FlowPane categoryContainer;
    @FXML
    ImageView profilePic;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // this.model.useDummyData(); // Simulate getting data from firebase
        try {
            this.model.setCategories(RestAPIHelper.getCategories(App.user.getIdToken()));
            this.model.setProducts(RestAPIHelper.getProducts(App.user.getIdToken()));
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.displayProducts("All");
        this.displayCategories();
        if (!App.user.profilePic.isEmpty()) {
            this.profilePic.setImage(new Image(App.user.profilePic));
        }
    }

    public void displayProducts() {
        System.out.println(this.currentCategory);
        this.displayProducts(this.currentCategory);
    }

    public void displayProducts(String filter) {
        this.table.getChildren().clear();
        this.productControllers.clear();
        System.out.println("displayProducts() " + this.model.getProducts().size());
        this.model.getProducts().forEach(pr -> {
            System.out.println(pr);
        });
        this.model.getProducts().forEach(product -> {
            if (filter.equals("All") || filter.equals(product.getCategory())) {
                try {
                    FXMLLoader productFXML = LoadFXMLHelper.loadFXML("productSettings.product");
                    ProductRowController tempController = new ProductRowController(product, this);
                    this.productControllers.add(tempController);
                    productFXML.setController(tempController);
                    this.table.getChildren().add(productFXML.load());
                } catch (Exception e) {
                    //TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }

    public void displayCategories() {
        var categories = this.model.getCategories();
        categories.forEach(category -> {
            Button catBtn = new Button(category.name);
            catBtn.setPrefSize(200.0, 50.0);
            catBtn.getStyleClass().addAll("firstbutton", "buttoncurve");
            categoryContainer.getChildren().add(catBtn);
        });

        categoryContainer.getChildren().forEach(catBtn -> {
            ((Button)catBtn).setOnAction((e) -> {
                this.displayProducts(((Button)catBtn).getText());
                this.currentCategory = ((Button)catBtn).getText();
            });
        });
    }
    
    public void editProduct(Product pr) {
        var scene = this.table.getScene();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("views/addProductsettings.fxml"));
        try {
            Node node = loader.load();
            AddProductSettingsController controller = loader.getController();
            controller.setPreFilledData(pr);
            scene.setRoot((Parent) node);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
