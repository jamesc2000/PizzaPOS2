package com.hiraya.pizzapos.takeOrders;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import com.hiraya.pizzapos.addProducts.ProductModel;

public class TakeOrdersModel {
    private class Category {
        private String name;
        private URL image;

        public URL getImage() {
            return image;
        }

        public void setImage(URL image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        Category(String name, URL image) {
            this.setName(name);
            this.setImage(image);
        }
    }

    private ArrayList<ProductModel> products = new ArrayList<ProductModel>();
    private ArrayList<Category> categories = new ArrayList<Category>();
    private ArrayList<Order> orders = new ArrayList<Order>();

    private Double subtotal;
    private Double discount;
    private Double vat;
    private Double total;

    //Dummy Data
    public void useDummyData() {
        ProductModel test1 = new ProductModel();
        ProductModel test2 = new ProductModel();
        ProductModel test3 = new ProductModel();

        try {
            test1.setValues("Pepperoni Pizza", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/368px-Google_2015_logo.svg.png", "Pizza", new ArrayList<>(Arrays.asList("Small", "Medium")), new ArrayList<>(Arrays.asList(100.00, 50.00)));
            test2.setValues("Cheese Pizza", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/368px-Google_2015_logo.svg.png", "Pizza", "Medium", 150.00);
            test3.setValues("Spaghetti", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/368px-Google_2015_logo.svg.png", "Pasta", "Small", 80.00);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.products.add(test1);
        this.products.add(test2);
        this.products.add(test3);
    }

    public ArrayList<ProductModel> getProducts() {
        return this.products;
    }

    /**
     * Initialize products and categories given a firebase products response
     */
    public void initProducts() {
        // TODO: Add parameter for input of firebase products, for now this is using dummy data


    }
}
