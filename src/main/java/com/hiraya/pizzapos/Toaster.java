package com.hiraya.pizzapos;

import java.io.IOException;

import com.hiraya.pizzapos.login.LoginController;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Toaster {
    // Toast service/spawner
    public static void spawnToast(Stage ownerStage, String toastTitle, String toastBody, int toastDelay, int fadeInDelay, int fadeOutDelay) throws IOException {
        Font poppinsRegular = Font.loadFont("file:resources/com/hiraya/pizzapos/fonts/Poppins-Regular.ttf", 16);
        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        FXMLLoader toastTemplate = new FXMLLoader(App.class.getResource("views/toastTemplate.fxml"));
        ToastController controller = new ToastController();
        toastTemplate.setController(controller);
        controller.test();
        controller.setTexts(toastTitle, toastBody);

        Scene scene = new Scene(toastTemplate.load());
        scene.setFill(Color.TRANSPARENT);

        toastStage.setScene(scene);

        toastStage.setY(ownerStage.getY() + 50);
        toastStage.setX(ownerStage.getX() + (ownerStage.getWidth() / 2) - (371 / 2));

        toastStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 1)); 
        fadeInTimeline.getKeyFrames().add(fadeInKey1);   
        fadeInTimeline.setOnFinished((ae) -> 
        {
            new Thread(() -> {
                try
                {
                    Thread.sleep(toastDelay);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                   Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 0)); 
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);   
                    fadeOutTimeline.setOnFinished((aeb) -> toastStage.close()); 
                    fadeOutTimeline.play();
            }).start();
        }); 
        fadeInTimeline.play();
    }
}
