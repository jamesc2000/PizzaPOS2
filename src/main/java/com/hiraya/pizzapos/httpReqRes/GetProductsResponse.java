package com.hiraya.pizzapos.httpReqRes;

import com.hiraya.pizzapos.App;

public class GetProductsResponse extends BaseResponse{
    public String transaction;
    public Document document;
    
    public class Document{
        public String name;
        public Fields fields;
    }
    
    public class Fields{
        public String StringValue;
    }
           
    
    
}
