package com.hiraya.pizzapos.takeOrders;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.transactionHistory.Transaction;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ReceiptController implements Initializable {
    ConfirmOrderPopupController parent;
    Transaction transaction;
    final DecimalFormat formatter = new DecimalFormat("P #0.00");
    public ReceiptController(ConfirmOrderPopupController confirmOrderPopupController, Transaction transaction) {
        this.parent = confirmOrderPopupController;
        this.transaction = transaction;
    }

    @FXML
    TextArea textArea;
    int n_cols;
    final int paddingFix = 0;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // System.out.println(Font.getFamilies());
        this.textArea.setText("");
        this.textArea.setFont(Font.font("Consolas", 12));
        n_cols = this.textArea.getPrefColumnCount() + paddingFix;

        this.printCentered("RECEIPT", true);
        this.printCentered(App.user.displayName, true);
        this.textArea.appendText("\n");
        this.printCentered("ORDERS", true);

        if (this.transaction.getOrders().size() > 0) {
            this.transaction.getOrders().forEach(o -> {
                this.printOrderRow(o);
            });
        }
        this.textArea.appendText("\n");

        this.printRightAligned("Subtotal: " + formatter.format(this.transaction.getSubtotal()));
        this.printRightAligned("VAT: " + formatter.format(this.transaction.getVatAmt()));
        this.printRightAligned("Less Senior Citizen/PWD: " + formatter.format(this.transaction.getDiscountAmt()));
        this.printRightAligned("Total: " + formatter.format(this.transaction.getTotal()));

    }

    private void printCentered(String text, Boolean insertNewLine) {
        final int textLength = text.length();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (this.n_cols / 2) - (textLength / 2); ++i) {
            sb.append(" ");
        }
        
        String startPadding = sb.toString();
        if (insertNewLine) {
            text += "\n";
        }
        this.textArea.appendText(startPadding + text);
    }

    private void printRightAligned(String text) {
        final int textLength = text.length();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.n_cols - (textLength + 2); ++i) {
            sb.append(" ");
        }
        sb.append(text);

        this.textArea.appendText(sb.toString() + "\n");
    }

    private void printOrderRow(Order o) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.name + " x " + o.quantity);
        int centerPos = this.n_cols / 2;
        int leftGap = centerPos - sb.length();
        for (int i = 0; i < leftGap; ++i) {
            sb.append(" ");
        }
        sb.append("-");
        int rightGap = (this.n_cols - centerPos) - formatter.format(o.price).length();
        for (int j = 0; j < rightGap - 2; ++j) {
            sb.append(" ");
        }
        sb.append(formatter.format(o.price));

        this.textArea.appendText(sb.toString() + "\n");
    }

    public void close() {
        Stage me = (Stage) this.textArea.getScene().getWindow();
        me.close();
    }
    
}
