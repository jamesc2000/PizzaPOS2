package com.hiraya.pizzapos.transactionHistory;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Router;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class TransactionHistoryController extends Router implements Initializable {
    ArrayList<Transaction> transactions;
    final private DecimalFormat formatter = new DecimalFormat("₱ #0.00");

    @FXML
    GridPane table;
    @FXML
    DatePicker picker;
    @FXML
    ImageView profilePic;

    LocalDate selectedDate;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        try {
            transactions = RestAPIHelper.getTransactions(App.user.getIdToken());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toaster.spawnToast("Error", e.getMessage(), "error");
        }

        picker.valueProperty().addListener((obs, oldVal, newVal) -> {
            this.selectedDate = newVal;
            this.displayData();
        });

        this.displayData();
        if (!App.user.profilePic.isEmpty()) {
            this.profilePic.setImage(new Image(App.user.profilePic));
        }
    }

    private void sortData() {
        // Descending only
        this.transactions.sort((d1, d2) -> d1.getTimestamp().compareTo(d2.getTimestamp()));
    }

    public void resetDate() {
        this.selectedDate = null;
        this.picker.setValue(null);
        this.displayData();
    }
    
    public void displayData() {
        Instant startOfDay = Instant.MIN;
        Instant endOfDay = Instant.MAX;
        if (this.selectedDate != null) {
            startOfDay = this.selectedDate.atStartOfDay().toInstant(ZoneOffset.ofHours(8));
            endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);
        }
        this.sortData();
        final int maxCol = 5;
        Transaction tr;
        table.getChildren().clear();
        int rowCounter = 0;
        for (int nTransaction = 0; nTransaction < this.transactions.size(); nTransaction++) {
            tr = this.transactions.get(nTransaction);

            if (tr.getTimestamp().isAfter(startOfDay) && tr.getTimestamp().isBefore(endOfDay)) {
                Label[] row = {
                    new Label(tr.getTimestampFormatted()),
                    new Label(tr.getTransactionRef()),
                    new Label(formatter.format(tr.getSubtotal())),
                    new Label(formatter.format(tr.getTotal())),
                    new Label(formatter.format(tr.getTotal() - tr.getSubtotal() + tr.getDiscountAmt())),
                };

                for (int col = 0; col < maxCol; col++) {
                    GridPane.setColumnIndex(row[col], col);
                    GridPane.setRowIndex(row[col], rowCounter);
                }

                this.table.getChildren().addAll(row);
                rowCounter++;
            }
        }
    }
}
