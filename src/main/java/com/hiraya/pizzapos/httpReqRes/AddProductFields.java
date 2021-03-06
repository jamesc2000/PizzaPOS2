package com.hiraya.pizzapos.httpReqRes;

import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hiraya.pizzapos.App;

@JsonIgnoreProperties({"price", "size"})
public class AddProductFields {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldValue {
        public String stringValue;
        public Double doubleValue;
        public Integer integerValue;
        public ArrayValue arrayValue;
        FieldValue(String val) { this.stringValue = val; }
        FieldValue(double val) { this.doubleValue = val; }
        FieldValue(int val) { this.integerValue = val; }
        public FieldValue() {}
    }

    public static class ArrayValue {
        public ArrayList<FieldValue> values;
        public ArrayValue() {}
    }

    public AddProductFields() { }

    public FieldValue name;
    public FieldValue imageUrl;
    public FieldValue category;
    public FieldValue sizes = new FieldValue();
    public FieldValue prices = new FieldValue();
    public FieldValue ownedBy = new FieldValue(App.user.getLocalId());

    public AddProductFields(
        String name,
        URL imageUrl,
        String category,
        ArrayList<String> sizes,
        ArrayList<Double> prices
    ) {
        this.name = new FieldValue(name);
        if (imageUrl == null) {
            this.imageUrl = new FieldValue("");
        } else {
            this.imageUrl = new FieldValue(imageUrl.toString());
        }
        this.category = new FieldValue(category);
        this.sizes.arrayValue = new ArrayValue();
        this.sizes.arrayValue.values = new ArrayList<FieldValue>();
        System.out.println("parameter sizes: " + sizes);
        sizes.forEach(size -> {
            if (size != null) {
                this.sizes.arrayValue.values.add(new FieldValue(size));
            }
        });
        this.prices.arrayValue = new ArrayValue();
        this.prices.arrayValue.values = new ArrayList<FieldValue>();
        System.out.println("parameter prices: " + prices);
        for (int i = 0; i < sizes.size(); ++i) {
            if (sizes.get(i) != null) {
                if (prices.get(i) != null) {
                    this.prices.arrayValue.values.add(new FieldValue(Double.valueOf(prices.get(i))));
                } else {
                    this.prices.arrayValue.values.add(new FieldValue(0.0));
                }
            }
        }
        // prices.forEach(price -> {
        //     if (price != null) {
        //         this.prices.arrayValue.values.add(new FieldValue(Double.valueOf(price)));
        //     }
        // });
    }

}
