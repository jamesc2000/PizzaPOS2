module com.hiraya.pizzapos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    
    // Add the line opens com.hiraya.pizzapos.<folder name> to javafx.fxml for every
    // folder that contains Controller classes, or else javafx won't be imported to them
    opens com.hiraya.pizzapos to javafx.fxml;
    opens com.hiraya.pizzapos.takeOrders to javafx.fxml;
    opens com.hiraya.pizzapos.login to javafx.fxml;
    opens com.hiraya.pizzapos.register to javafx.fxml;
    opens com.hiraya.pizzapos.newLogin to javafx.fxml;
    opens com.hiraya.pizzapos.helpers to com.fasterxml.jackson.databind;
    opens com.hiraya.pizzapos.httpReqRes to com.fasterxml.jackson.databind;
    exports com.hiraya.pizzapos;
}
