package com.hiraya.pizzapos;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AppController {
    
    @FXML
    private Button logout;
    
    @FXML
    private void logout() throws IOException {
        App.setRoot("newLogin");
    }
    
}
