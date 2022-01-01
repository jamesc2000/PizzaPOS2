package com.hiraya.pizzapos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXML;

public class ToastController implements Initializable {
    @FXML
    Text titleText;
    @FXML
    Text bodyText;

    private String tempTitle;
    private String tempBody;

    // https://stackoverflow.com/a/30815504 Solution #1
    public void setTexts(String title, String body) {
        this.tempBody = body;
        this.tempTitle = title;
    }

    public void close() {
        Stage toast = (Stage)this.titleText.getScene().getWindow();
        toast.close();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.titleText.setText(tempTitle);
        this.bodyText.setText(tempBody);
    }
}
