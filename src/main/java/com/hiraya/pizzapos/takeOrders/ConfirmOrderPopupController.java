package com.hiraya.pizzapos.takeOrders;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.transactionHistory.Transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmOrderPopupController implements Initializable {
    final private DecimalFormat formatter = new DecimalFormat("₱ #0.00");
    TakeOrdersController parent;

    private String selectedMode = "Cash";

    @FXML
    Label amountDue, change;
    @FXML
    TextField paid;
    @FXML
    TextField accntNo, expDateCard, cvc;
    @FXML
    ComboBox<String> modePayment;
    @FXML
    CheckBox savePdf;

    public ConfirmOrderPopupController(TakeOrdersController parent) {
        this.parent = parent;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.amountDue.setText(formatter.format(this.parent.model.currTransaction.getTotal()));
        this.change.setText(formatter.format(0.00));

        this.modePayment.getItems().clear();
        this.modePayment.getItems().addAll("Cash", "Credit Card", "Debit Card");
        this.modePayment.getSelectionModel().select("Cash");
        this.updateCardFields();

        this.paid.textProperty().addListener((obs, oldVal, newVal) -> {
            this.updateChange();
        });

        this.modePayment.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.selectedMode = newVal;
            this.updateCardFields();
        });
    }

    private void updateChange() {
        Double change =  Double.valueOf(this.paid.getText()) - Double.valueOf(this.amountDue.getText().split(" ")[1]);
        this.change.setText(formatter.format(change));
    }

    private void updateCardFields() {
        if (this.selectedMode.equals("Cash")) {
            this.accntNo.setDisable(true);
            this.cvc.setDisable(true);
            this.expDateCard.setDisable(true);
        } else {
            this.accntNo.setDisable(false);
            this.cvc.setDisable(false);
            this.expDateCard.setDisable(false);
        }
    }

    public void close() {
        Stage popup = (Stage) this.amountDue.getScene().getWindow();
        popup.close();
    }

    public void pay() {
        this.parent.setTransactionPayment(Double.valueOf(this.paid.getText()), Double.valueOf(this.change.getText().split(" ")[1]));
        try {
            this.parent.model.currTransaction.checkout(App.user.getIdToken());
            Toaster.spawnToast("Order Success", "", "success");
            if (this.savePdf.isSelected()) {
                this.makeReceipt(this.parent.model.currTransaction);
            }
            // this.makePdfAndOpen(this.parent.model.currTransaction);
            this.parent.model.currTransaction = new Transaction();
            this.parent.model.clearOrders();
            this.parent.model.currTransaction.recomputeOrders(); // Lazy fix for weird bug
            this.parent.clearOrderSummaryView();
            this.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeReceipt(Transaction transaction) {
        ReceiptController sel = new ReceiptController(this, transaction);
        Stage popup = new Stage();
        popup.initOwner(App.getPrimaryStage());
        popup.setResizable(false);
        popup.initStyle(StageStyle.TRANSPARENT);

        // System.out.println(App.class.getResource("views/uploadImage.fxml").toExternalForm());
        FXMLLoader fxml = new FXMLLoader(App.class.getResource("views/takeOrders.receipt.fxml"));
        fxml.setController(sel);
        Scene scene;
        try {
            scene = new Scene(fxml.load());
            scene.setFill(Color.TRANSPARENT);
            popup.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            Toaster.spawnToast("FXML Error", e.getMessage(), "error");
        }
        popup.setOnHiding((event) -> {
            System.out.println("Closed");
        });
        popup.show();


    }
}
