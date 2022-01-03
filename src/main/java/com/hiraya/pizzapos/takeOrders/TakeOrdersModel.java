package com.hiraya.pizzapos.takeOrders;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.hiraya.pizzapos.productSettings.Category;
import com.hiraya.pizzapos.productSettings.Product;
import com.hiraya.pizzapos.transactionHistory.Transaction;

public class TakeOrdersModel {
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Category> categories = new ArrayList<Category>();
    private ArrayList<Order> orders = new ArrayList<Order>();
    public Transaction currTransaction = new Transaction();

    public void checkout() {

        // Send 
    }

    //Dummy Data
    public void useDummyData() {
        Product test1 = new Product();
        Product test2 = new Product();
        Product test3 = new Product();

        try {
            test1.setValues("a", "Pepperoni Pizza", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/368px-Google_2015_logo.svg.png", "Pizza", new ArrayList<>(Arrays.asList("Small", "Medium")), new ArrayList<>(Arrays.asList(100.00, 50.00)));
            test2.setValues("b", "Cheese Pizza", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/368px-Google_2015_logo.svg.png", "Pizza", "Medium", 150.00);
            test3.setValues("c", "Spaghetti", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/368px-Google_2015_logo.svg.png", "Pasta", "Small", 80.00);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.products.add(test1);
        this.products.add(test2);
        this.products.add(test3);
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Order> getOrders() {
        return this.orders;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void appendOrder(Order o) {
        this.orders.add(o);
    }

    public void removeOrder(Order o) {
        this.orders.remove(o);
        System.out.println("Orders now: ");
        this.orders.forEach((e) -> {
            System.out.println(e.name);
        });
    }

    public void appendProduct(Collection<Order> o) {
        this.orders.addAll(o);
    }
}
