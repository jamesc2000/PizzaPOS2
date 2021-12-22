package com.hiraya.pizzapos.takeOrders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.addProducts.ProductModel;
import com.hiraya.pizzapos.helpers.LoadFXMLHelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
    
public class TakeOrdersController {
    TakeOrdersModel model = new TakeOrdersModel();

    ArrayList<MenuOrderController> productControllers = new ArrayList<MenuOrderController>();

    @FXML
    private FlowPane menuContainer;
    @FXML
    private HBox categoryContainer;

    @FXML
    private void initialize() {
        this.model.useDummyData();
        System.out.println("Dashboard Initialized!");
        this.displayData();
        this.displayCategories();
    }

    public void switchToTemporary() throws IOException {
        App.setRoot("addProductsSample");
    }

    /**
     * Displays products and categories in the model
     */
    private void displayData() {
        FXMLLoader orderItem;
        for (int i = 0; i < this.model.getProducts().size(); ++i) {
            try {
                orderItem = LoadFXMLHelper.loadFXML("takeOrders.order");
                // System.out.println(this.model.getProducts().get(i).getName());
                this.productControllers.add(new MenuOrderController(this.model.getProducts().get(i)));
                System.out.println("Controller: " + this.productControllers.get(i));
                orderItem.setController(this.productControllers.get(i));
                // System.out.println("11111");
                this.menuContainer.getChildren().add(orderItem.load());
            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    private Set<String> groupCategories() {
        var products = this.model.getProducts();
        Set<String> categories = new HashSet<String>();
        products.forEach(product -> {
            categories.add(product.getCategory());
        });
        System.out.println(categories);
        return categories;
    }

    private void displayCategories() {
        var categories = groupCategories();
        categories.forEach(category -> {
            // TODO: Graphic/img ng category
            ImageView graphic = new ImageView(App.class.getResource("images/userProfilepic.png").toExternalForm());
            graphic.setFitHeight(91.0);
            graphic.setFitWidth(91.0);
            graphic.setPickOnBounds(true);
            graphic.setPreserveRatio(true);
            Button catBtn = new Button(category, graphic);
            catBtn.setContentDisplay(ContentDisplay.TOP);
            catBtn.setPrefSize(195.0, 155.0);
            catBtn.setStyle("-fx-background-radius: 15px;");
            catBtn.getStyleClass().add("takeorder");

            categoryContainer.getChildren().add(catBtn);
        });
    }
    // private ArrayList<ProductModel> groupSizes(ArrayList<ProductModel> products) {
    //     // Sort the products by name, and then combine consecutive items
    //     ArrayList<ProductModel> grouped = new ArrayList<ProductModel>();
    //     products.sort(new Comparator<ProductModel>() {
    //         @Override
    //         public int compare(ProductModel lhs, ProductModel rhs) {
    //             return lhs.getName().compareToIgnoreCase(rhs.getName());
    //         }
    //     });
    //     System.out.println(products.toString());
    //     for (int i = 0; i < products.size() - 1; ++i) {
    //         if (products.get(i).getName().equalsIgnoreCase(products.get(i + 1).getName())) {
    //             grouped.add()
    //         } else {
    //             grouped.add(products.get(i));
    //         }
    //     }
    //     System.out.println(grouped.toString());
    //     return grouped;
    // }
}
