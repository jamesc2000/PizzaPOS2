package com.hiraya.pizzapos.httpReqRes;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hiraya.pizzapos.App;

public class UserFields {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldValue {
        public String stringValue;
        public Double doubleValue;
        public ArrayValue arrayValue;
        FieldValue(String val) { this.stringValue = val; }
        FieldValue(double val) { this.doubleValue = val; }
        public FieldValue() {}
    }

    public static class ArrayValue {
        public ArrayList<FieldValue> values;
        public ArrayValue() {}
    }

    public UserFields() { }

    public FieldValue name;
    public FieldValue email;
    public FieldValue contact;
    public FieldValue imageUrl;
    public FieldValue ownedBy;

    public UserFields(
        String name,
        String email,
        String contact,
        String imageUrl,
        String localId
    ) {
        this.name = new FieldValue(name);
        this.email = new FieldValue(email);
        this.contact = new FieldValue(contact);
        this.imageUrl = new FieldValue(imageUrl);
        if (App.user.getLocalId() == null) {
            this.ownedBy = new FieldValue(localId);
        } else {
            this.ownedBy = new FieldValue(App.user.getLocalId());
        }
    }
}
