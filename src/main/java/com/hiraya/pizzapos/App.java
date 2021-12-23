package com.hiraya.pizzapos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    // This loggedInUser has the same lifetime as the app itself
    public static CurrentUser user = new CurrentUser();

    @Override
    public void start(Stage stage) throws IOException {
        App.primaryStage = stage;

        App.primaryStage.setTitle("PizzaPOS");
//        stage.setMaximized(true);
//       stage.setFullScreen(true);
        
        
        scene = new Scene(loadFXML("takeOrders"), 1000, 600);
        App.primaryStage.setScene(scene);
        App.primaryStage.show();
        Toaster.spawnToast("test", "body", "error");
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
//        System.out.println(App.class.getResource("views/" + fxml + ".fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        // Load fonts
        Font.loadFont(App.class.getResourceAsStream("fonts/Poppins-Regular.ttf"), 16);
        // === 
        launch();
    }
}