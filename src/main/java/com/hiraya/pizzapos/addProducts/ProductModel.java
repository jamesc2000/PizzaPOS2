package com.hiraya.pizzapos.addProducts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.AddProductFields;
import com.hiraya.pizzapos.httpReqRes.FirestoreRequest;

public class ProductModel {
    private String name;
    private URL imageUrl;
    private String category;
    private String size;
    private double price;

    ProductModel() { }

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
}
