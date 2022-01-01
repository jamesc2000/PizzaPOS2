package com.hiraya.pizzapos.takeOrders;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.helpers.LoadFXMLHelper;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.productSettings.Product;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
    
public class TakeOrdersController {
    TakeOrdersModel model = new TakeOrdersModel();

    ArrayList<MenuOrderController> productControllers = new ArrayList<MenuOrderController>();

    @FXML
    private FlowPane menuContainer;
    @FXML
    private HBox categoryContainer;
    @FXML
    private GridPane orderSummary;

    @FXML
    private void initialize() {
        this.model.useDummyData();
        System.out.println("Dashboard Initialized!" + this.toString());
        try {
            this.model.setCategories(RestAPIHelper.getCategories(App.user.getIdToken()));
            this.model.setProducts(RestAPIHelper.getProducts(App.user.getIdToken()));
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.displayData();
        this.displayCategories();
        this.displayTransaction();
        // this.addOrder(new Order());
    }

    public void switchToTemporary() throws IOException {
        App.setRoot("productSettings");
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
                this.productControllers.add(new MenuOrderController(this.model.getProducts().get(i), this));
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
        var categories = this.model.getCategories();
        categories.forEach(category -> {
            // TODO: Graphic/img ng category
            ImageView graphic = new ImageView();
            if (category.imageUrl != null) {
                graphic.setImage(new Image(category.imageUrl.toExternalForm()));
            } else {
                graphic.setImage(new Image(App.class.getResource("images/addProduct.JPG").toExternalForm()));
            }
            graphic.setFitHeight(91.0);
            graphic.setFitWidth(91.0);
            graphic.setPickOnBounds(true);
            graphic.setPreserveRatio(true);
            Button catBtn = new Button(category.name, graphic);
            catBtn.setContentDisplay(ContentDisplay.TOP);
            catBtn.setPrefSize(195.0, 155.0);
            catBtn.setStyle("-fx-background-radius: 15px;");
            catBtn.getStyleClass().add("takeorder");

            categoryContainer.getChildren().add(catBtn);
        });
    }

    public void addOrder(Order order) {
        // Push a product to model.products, then redisplay order summary
        var currOrders = this.model.getOrders();
        // Find if there is an order with same name and size
        int i = 0;
        while (i < currOrders.size()) {
            if (currOrders.get(i).name.equals(order.name) && currOrders.get(i).size.equals(order.size)) {
                currOrders.get(i).quantity++;
                Spinner<Integer> spinner = (Spinner<Integer>)this.orderSummary.getChildren().get(i*5 + 2);
                System.out.println("Found: " + spinner.toString());
                spinner.increment();
                this.model.currTransaction.setOrder(i, currOrders.get(i));
                this.displayTransaction();
                return; // If found, dont proceed below, return early
            }
            ++i;
        }

        this.model.appendOrder(order);
        final int currRow = this.model.getOrders().size();
        final Node[] row = {
            new Label(order.name),
            new Label(order.size),
            new Spinner<Integer>(1, 100, 1),
            new Label("₱ " + order.price.toString()),
            new Button("X")
        };
        for (int j = 0; j < row.length; ++j) {
            GridPane.setColumnIndex(row[j], j);
            GridPane.setRowIndex(row[j], currRow);
        }
        this.model.currTransaction.addOrder(order);
        this.orderSummary.getChildren().addAll(row);
        this.displayTransaction();
    }

    @FXML
    private Label subtotalField;
    @FXML
    private Label vatField;
    @FXML
    private Label discountField;
    @FXML
    private Label totalField;

    final private DecimalFormat formatter = new DecimalFormat("#0.00");
    private void displayTransaction() {
        this.subtotalField.setText("₱ " + formatter.format(this.model.currTransaction.getSubtotal()));
        this.vatField.setText("₱ " + formatter.format(this.model.currTransaction.getVatAmt()));
        this.discountField.setText("₱ " + formatter.format(this.model.currTransaction.getDiscountAmt()));
        this.totalField.setText("₱ " + formatter.format(this.model.currTransaction.getTotal()));
    }
}
