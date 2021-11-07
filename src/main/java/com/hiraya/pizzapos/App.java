package com.hiraya.pizzapos;

import com.hiraya.pizzapos.helpers.LoginData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import com.hiraya.pizzapos.helpers.RestAPIHelper;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("PizzaPOS");
        stage.setMaximized(true);
        
        scene = new Scene(loadFXML("App"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println(App.class.getResource("views/" + fxml + ".fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        LoginData.LoginResponse triedUser = null;
        try {
            triedUser = RestAPIHelper.login("cruz.james99@gmail.com", "testtest123strong!");
        } catch (InterruptedException | IOException e) {
            System.out.println(e);
        }
        
        // Check output to see entire http response
        System.out.println(triedUser.email);
        System.out.println(triedUser.error.code);
        launch();
    }
}