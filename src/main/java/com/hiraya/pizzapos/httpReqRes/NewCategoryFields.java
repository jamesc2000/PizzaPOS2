package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.httpReqRes.GetCategoriesRequest.Field;

public class NewCategoryFields {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class FieldValue {
        public String stringValue;
        public Double doubleValue;
        FieldValue(String val) { this.stringValue = val; }
        FieldValue(double val) { this.doubleValue = val; }
        public FieldValue() {}
    }

    public FieldValue name;
    public FieldValue imageUrl;
    public FieldValue ownedBy = new FieldValue(App.user.getLocalId());
    // TODO: Sizes and Prices should be array in DB as well
    public NewCategoryFields(String name, String imageUrl) {
        this.name = new FieldValue(name);
        this.imageUrl = new FieldValue(imageUrl);
    }

    public NewCategoryFields() {}
}
