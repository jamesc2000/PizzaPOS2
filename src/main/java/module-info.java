module com.hiraya.pizzapos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.hiraya.pizzapos to javafx.fxml;
    opens com.hiraya.pizzapos.takeOrders to javafx.fxml;
    exports com.hiraya.pizzapos;
}
