package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseResponse {
    public BaseResponse() { }
    
    public static class ErrorListElem {
        public String message;
        public String domain;
        public String reason;
    }

    public static class Error {
        public String code;
        public String message;
        public ArrayList<ErrorListElem> errors;
    }

    public Error error;

    public Boolean isSuccessful() {
        if (this.error.message == null) {
            return true;
        } else {
            return false;
        }
    }
}
