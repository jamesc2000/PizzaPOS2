package com.hiraya.pizzapos.transactionHistory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.NewTransactionFields;
import com.hiraya.pizzapos.httpReqRes.GetTransactionsResponse.Document;
import com.hiraya.pizzapos.takeOrders.Order;

public class Transaction {
    private Instant timestamp; // Used only in history, not in takeOrders
    private String transactionRef;
    private ArrayList<Order> orders = new ArrayList<Order>();
    private double total = 0;
    private double subtotal = 0;
    private double amountPaid = 0;
    private double amountChange = 0;
    private double discountAmt = 0;
    final private double discountPercent = 0.2;
    final private double VAT = 0.12;

    final private DateTimeFormatter df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        .withLocale(Locale.US)
        .withZone(ZoneId.systemDefault());

    public Transaction() {
        this.transactionRef = generateRef();
    }

    public Transaction(ArrayList<Order> orders) {
        this.transactionRef = generateRef();
        this.orders = orders;
        this.orders.forEach(order -> {
            this.subtotal += order.price * order.quantity;
        });
        this.total = this.subtotal * (1 + this.VAT);
    }

    public Transaction(Document doc) {
        this.transactionRef = doc.fields.transactionRef.stringValue;
        this.total = doc.fields.total.doubleValue;
        this.subtotal = doc.fields.subtotal.doubleValue;
        this.amountPaid = doc.fields.amountPaid.doubleValue;
        this.amountChange = doc.fields.amountChange.doubleValue;
        this.discountAmt = doc.fields.discountAmt.doubleValue;
        try {
            this.timestamp = Instant.parse(doc.createTime);
        } catch (DateTimeParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void recomputeOrders() {
        this.subtotal = 0;
        this.total = 0;
        this.discountAmt = 0;
        this.orders.forEach(order -> {
            this.subtotal += order.price * order.quantity;
        });
        this.total = this.subtotal * (1 + this.VAT);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrder(int idx, Order newOrder) {
        this.orders.set(idx, newOrder);
        // this.orders.forEach(order -> {
        //     this.subtotal += order.price * order.quantity;
        // });
        // this.total = this.subtotal * (1 + this.VAT);
        this.recomputeOrders();
    }

    public void addOrder(Order order) {
        // this.subtotal += order.price * order.quantity;
        // this.total = this.subtotal * (1 + this.VAT);
        this.orders.add(order);
        this.recomputeOrders();
    }

    public void removeOrder(Order order) {
        // Double price = order.price;
        // Integer qty = order.quantity;
        Boolean orderRemoved = this.orders.remove(order);
        if (orderRemoved) {
            // System.out.println("order removed in transaction");
            // this.subtotal -= price * qty;
            // this.total = this.subtotal * (1 + this.VAT);
            this.recomputeOrders();
        }
    }

    public void clearOrders() {
        this.orders.clear();
    }

    public void applyDiscount() {
        this.discountAmt = this.subtotal * this.discountPercent;
        this.total -= this.discountAmt;
    }

    public void removeDiscount() {
        this.total += this.discountAmt;
        this.discountAmt = 0;
    }

    private static String generateRef() {
        int lb = 65;
        int ub = 90;
        Random rand = new Random();
        StringBuilder temp = new StringBuilder(8);
        for (int i = 0; i < 8; ++i) {
            int randomInt = lb + (int)(rand.nextFloat() * (ub - lb + 1));
            temp.append((char)randomInt);
        }
        return temp.toString();
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountChange() {
        return amountChange;
    }

    public void setAmountChange(double amountChange) {
        this.amountChange = amountChange;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(double discountAmt) {
        this.discountAmt = discountAmt;
    }

    public double getVatAmt() {
        return this.VAT * this.subtotal;
    }

    public void checkout(String idToken) throws Exception {
        if (this.amountPaid < (Math.round(this.total * 100) / 100)) {
            Toaster.spawnToast("Invalid amount.", "Amount paid must be greater than total", "error");
            throw new Exception("Invalid amount");
        }
        NewTransactionFields fields = new NewTransactionFields(this);
        try {
            RestAPIHelper.createTransaction(fields, idToken);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toaster.spawnToast("Error in confirming order", e.getLocalizedMessage(), "error");
        }
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestampFormatted() {
        String out = df.format(this.timestamp);
        return out;
    }
}
