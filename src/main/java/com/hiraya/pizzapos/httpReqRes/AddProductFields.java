package com.hiraya.pizzapos.httpReqRes;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hiraya.pizzapos.App;

public class AddProductFields {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class FieldValue {
        public String stringValue;
        public Double doubleValue;
        FieldValue(String val) { this.stringValue = val; }
        FieldValue(double val) { this.doubleValue = val; }
    }

    AddProductFields() { }

    public FieldValue name;
    public FieldValue imageUrl;
    public FieldValue category;
    public FieldValue size;
    public FieldValue price;
    public FieldValue ownedBy = new FieldValue(App.user.getLocalId());

    public AddProductFields(
        String name,
        URL imageUrl,
        String category,
        String size,
        double price
    ) {
        this.name = new FieldValue(name);
        this.imageUrl = new FieldValue(imageUrl.toString());
        this.category = new FieldValue(category);
        this.size = new FieldValue(size);
        this.price = new FieldValue(price);
    }
}
