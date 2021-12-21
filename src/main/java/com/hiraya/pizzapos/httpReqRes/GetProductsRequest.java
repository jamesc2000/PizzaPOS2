package com.hiraya.pizzapos.httpReqRes;

import com.hiraya.pizzapos.App;

public class GetProductsRequest extends BaseRequest {
    public class StructuredQuery {
        public From from;
        public Where where;
    }

    public class From {
        public final String collectionId = App.user.getLocalId();
        public final Boolean allDescendants = true;
    }
    
    public class Where {
        public FieldFilter fieldFilter;
    }

    public class FieldFilter {
        public Field field;
        public String op;
        public Value value;
        
    }

    public class Field {
        public String fieldPath;
    }
    
    public class Value {
        public String StringValue;
    }
  
    public StructuredQuery structuredQuery;
}
