package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;

@JsonInclude(Include.NON_NULL)
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
}
