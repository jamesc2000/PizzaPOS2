module com.hiraya.pizzapos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    
    opens com.hiraya.pizzapos to javafx.fxml;
    opens com.hiraya.pizzapos.takeOrders to javafx.fxml;
    opens com.hiraya.pizzapos.helpers to com.fasterxml.jackson.databind;
    exports com.hiraya.pizzapos;
}
