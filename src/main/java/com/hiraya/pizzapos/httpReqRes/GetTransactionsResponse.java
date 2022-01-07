package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;;

@JsonInclude(Include.NON_NULL)
public class GetTransactionsResponse extends BaseResponse {
    public class Document {
        public String name;
        public NewTransactionFields fields = new NewTransactionFields();
        public String createTime;
        public String updateTime;
    }
    public Document document = new Document();
    public String readTime;
    
}
