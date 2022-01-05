package com.hiraya.pizzapos.httpReqRes;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.takeOrders.Order;
import com.hiraya.pizzapos.transactionHistory.Transaction;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewTransactionFields {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldValue {
        public String stringValue;
        public Double doubleValue;
        public ArrayValue arrayValue;
        public MapValue mapValue;
        FieldValue(String val) { this.stringValue = val; }
        FieldValue(double val) { this.doubleValue = val; }
        FieldValue(Order o) { 
            this.mapValue = new MapValue();
            this.mapValue.fields = new OrderFields(o);
        }
        public FieldValue() {}
    }

    public static class ArrayValue {
        public ArrayList<FieldValue> values;
        public ArrayValue() {}
    }

    public static class MapValue {
        public OrderFields fields;
        public MapValue() {}
    }

    public static class OrderFields {
        public FieldValue name;
        public FieldValue size;
        public FieldValue quantity;
        public FieldValue price;
        public OrderFields() {}
        public OrderFields(Order o) {
            this.name = new FieldValue(o.name);
            this.size = new FieldValue(o.size);
            this.quantity = new FieldValue(o.quantity);
            this.price = new FieldValue(o.price.doubleValue());
        }
    }

    public FieldValue orders = new FieldValue();
    public FieldValue transactionRef;
    public FieldValue total;
    public FieldValue subtotal;
    public FieldValue amountPaid;
    public FieldValue amountChange;
    public FieldValue discountAmt;
    public FieldValue ownedBy = new FieldValue(App.user.getLocalId());

    public NewTransactionFields(Transaction t) {
        this.transactionRef = new FieldValue(t.getTransactionRef());
        this.orders.arrayValue = new ArrayValue();
        this.orders.arrayValue.values = new ArrayList<>();
        t.getOrders().forEach(order -> {
            this.orders.arrayValue.values.add(new FieldValue(order));
        });
        this.total = new FieldValue(t.getTotal());
        this.subtotal = new FieldValue(t.getSubtotal());
        this.amountPaid = new FieldValue(t.getAmountPaid());
        this.amountChange = new FieldValue(t.getAmountChange());
        this.discountAmt = new FieldValue(t.getDiscountAmt());
    }

    public NewTransactionFields() {
    }
}
