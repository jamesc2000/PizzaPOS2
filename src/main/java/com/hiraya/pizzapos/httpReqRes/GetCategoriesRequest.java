
package com.hiraya.pizzapos.httpReqRes;

import com.hiraya.pizzapos.App;

public class GetCategoriesRequest extends BaseRequest {
    public class StructuredQuery {
        public From from = new From();
        public Where where = new Where();
    }

    public class From {
        public String collectionId;
        public final Boolean allDescendants = true;
    }
    
    public class Where {
        public FieldFilter fieldFilter = new FieldFilter();
    }

    public class FieldFilter {
        public Field field = new Field();
        public String op;
        public Value value = new Value();
        
    }

    public class Field {
        public String fieldPath;
    }
    
    public class Value {
        public String stringValue;
    }
  
    public StructuredQuery structuredQuery = new StructuredQuery();

    public GetCategoriesRequest() {
        this.structuredQuery.from.collectionId = "categories";
        this.structuredQuery.where.fieldFilter.field.fieldPath = "ownedBy";
        this.structuredQuery.where.fieldFilter.op = "EQUAL";
        this.structuredQuery.where.fieldFilter.value.stringValue = App.user.getLocalId();
    }
}
