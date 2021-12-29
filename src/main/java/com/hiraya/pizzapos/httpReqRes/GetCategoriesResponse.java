package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GetCategoriesResponse extends BaseResponse {
    public class Document {
        public String name;
        public NewCategoryFields fields = new NewCategoryFields("", "");
        public String createTime;
        public String updateTime;
    }
    public Document document = new Document();
    public String readTime;
}
