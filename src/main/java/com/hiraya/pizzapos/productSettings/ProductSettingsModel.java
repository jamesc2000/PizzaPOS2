package com.hiraya.pizzapos.productSettings;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductSettingsModel {
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Category> categories = new ArrayList<Category>();

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

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
}
