package com.hiraya.pizzapos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    // This loggedInUser has the same lifetime as the app itself
    public static CurrentUser user = new CurrentUser();

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("PizzaPOS");
//        stage.setMaximized(true);
//       stage.setFullScreen(true);
        
        
        scene = new Scene(loadFXML("login"), 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println(App.class.getResource("views/" + fxml + ".fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}