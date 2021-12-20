package com.hiraya.pizzapos.helpers;

import java.io.IOException;

import com.hiraya.pizzapos.App;

import javafx.fxml.FXMLLoader;

public class LoadFXMLHelper {
    public static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
        return fxmlLoader;
    }
}
