package com.hiraya.pizzapos.httpReqRes;

import com.hiraya.pizzapos.App;

public class GetProductsRequest extends BaseRequest {
    public class StructuredQuery {
        public From from;
        public Where where;
    }

    public class From {
        public String collectionId = App.user.getLocalId();
        public Boolean allDescendants = true;
    }
    
    public class Where {
        public FieldFilter fieldFilter;
    }

    public class FieldFilter {
        public Field field;
    }

    public class Field {
        //fieldpath dito
    }

    public StructuredQuery structuredQuery;
}
