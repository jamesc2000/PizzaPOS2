package com.hiraya.pizzapos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    // This loggedInUser has the same lifetime as the app itself
    public static CurrentUser user = new CurrentUser();
    // Global executor service, so we don't need to reserve new separate
    // threads each controller. 
    public static ExecutorService bgThreads = Executors.newFixedThreadPool(3);

    @Override
    public void start(Stage stage) throws IOException {
        App.primaryStage = stage;

        App.primaryStage.setTitle("PizzaPOS");
//        stage.setMaximized(true);
//       stage.setFullScreen(true);
        
        stage.getIcons().add(new Image(App.class.getResourceAsStream("images/logo.png")));
        
        scene = new Scene(loadFXML("login"), 1000, 600);
        App.primaryStage.setScene(scene);
        App.primaryStage.show();
    }

    @Override
    public void stop() throws java.lang.Exception {
        super.stop();
        App.primaryStage.close();
        // After app main thread closes, we also close the background threads
        // so we free system resources (ram, cpu, etc)
        App.bgThreads.awaitTermination(1, TimeUnit.SECONDS);  // Ask the bgThreads politely to finish their work in 1 second
        App.bgThreads.shutdown(); // Shutdown the bgThreads by force (respectfully)
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
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