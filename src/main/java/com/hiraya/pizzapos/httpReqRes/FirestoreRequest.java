package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FirestoreRequest<Fields> extends BaseRequest {
    public String name;
    public Fields fields;
    public String createTime;
    public String updateTime;

    public FirestoreRequest(Fields fields) {
        this.fields = fields;
    }

    public FirestoreRequest() {}
}
