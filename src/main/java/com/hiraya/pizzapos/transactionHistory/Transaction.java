package com.hiraya.pizzapos.transactionHistory;

import java.util.ArrayList;
import java.util.Random;

import com.hiraya.pizzapos.takeOrders.Order;

public class Transaction {
    private String transactionRef;
    private ArrayList<Order> orders = new ArrayList<Order>();
    private double total = 0;
    private double subtotal = 0;
    private double amountPaid = 0;
    private double amountChange = 0;
    private double discountAmt = 0;
    final private double discountPercent = 0.2;
    final private double VAT = 0.12;

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

    public void setOrder(int idx, Order newOrder) {
        this.orders.set(idx, newOrder);
        this.orders.forEach(order -> {
            this.subtotal += order.price * order.quantity;
        });
        this.total = this.subtotal * (1 + this.VAT);
    }

    public void addOrder(Order order) {
        this.subtotal += order.price * order.quantity;
        this.total = this.subtotal * (1 + this.VAT);
        this.orders.add(order);
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
}