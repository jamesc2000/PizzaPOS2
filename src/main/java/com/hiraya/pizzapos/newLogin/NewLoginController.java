package com.hiraya.pizzapos.newLogin;

import com.hiraya.pizzapos.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NewLoginController {

    @FXML
    private Button pinkButton;

    public void displayHello() {
        System.out.println("Hello");
    }

    public void switchToApp() throws IOException {
        App.setRoot("App");
    }
    
}
