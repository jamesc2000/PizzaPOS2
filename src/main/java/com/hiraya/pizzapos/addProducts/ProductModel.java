package com.hiraya.pizzapos.addProducts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.AddProductFields;
import com.hiraya.pizzapos.httpReqRes.FirestoreRequest;

public class ProductModel {
    private String name;
    private URL imageUrl;
    private String category;
    //TODO: Migrate to array type for size and price
    private String size;
    private ArrayList<String> sizesArray = new ArrayList<String>(); // Used only in takeOrders screen, but raw data doesnt have this
    private double price;
    private ArrayList<Double> pricesArray = new ArrayList<Double>(); // Same note as sizesArray

    public ProductModel() { }

    public String getName() { return this.name; }
    public URL getImage() { return this.imageUrl; }
    public String getCategory() { return this.category; }
    public String getSize() { return this.size; }

    public ArrayList<String> getSizesArr() { return this.sizesArray; }
    public void pushSize(String s) { this.sizesArray.add(s); }

    public double getPrice() { return this.price; }

    public ArrayList<Double> getPricesArr() { return this.pricesArray; }
    public void pushPrice(double p) { this.pricesArray.add(Double.valueOf(p)); }

    public void setValues(
        String name,
        String imageUrl,
        String category,
        String size,
        String price
    ) throws MalformedURLException {
        this.name = name;
        this.imageUrl = new URL(imageUrl);
        this.category = category;
        this.size = size;
        this.price = Double.parseDouble(price);
    }

    public void setValues(
        String name,
        String imageUrl,
        String category,
        String size,
        Double price
    ) throws MalformedURLException {
        this.name = name;
        this.imageUrl = new URL(imageUrl);
        this.category = category;
        this.sizesArray.add(size);
        this.pricesArray.add(price);
    }

    public void setValues(
        String name,
        String imageUrl,
        String category,
        ArrayList<String> sizes,
        ArrayList<Double> prices
    ) throws MalformedURLException {
        this.name = name;
        this.imageUrl = new URL(imageUrl);
        this.category = category;
        this.sizesArray = sizes;
        this.pricesArray = prices;
    }

    // TODO: This shouldnt be void, but will return something
    public void sendToFirestore() {
        AddProductFields fields = new AddProductFields(
            this.name,
            this.imageUrl,
            this.category,
            this.size,
            this.price
        );

        try {
            System.out.println("Product send func");
            System.out.println(App.user.getIdToken());
            RestAPIHelper.createProduct(fields, App.user.getIdToken());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        // For debugging only
        String str = this.name + "\n";
        for (int i = 0; i < this.sizesArray.size(); ++i) {
            str += " - " + this.sizesArray.get(i) + " " + this.pricesArray.get(i) + "\n";
        }
        return str + "\n";
    }
}
