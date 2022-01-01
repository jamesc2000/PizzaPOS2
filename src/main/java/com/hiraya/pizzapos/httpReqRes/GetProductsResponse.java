package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GetProductsResponse extends BaseResponse{
    public class Document {
        public String name;
        public AddProductFields fields = new AddProductFields();
        public String createTime;
        public String updateTime;
    }
    public Document document = new Document();
    public String readTime;
}
