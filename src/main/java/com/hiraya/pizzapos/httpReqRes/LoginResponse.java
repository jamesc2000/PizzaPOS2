package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;

@JsonInclude(Include.NON_NULL)
public class LoginResponse {
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
    
    // Used only in successful attempts
    public String email;
    public String kind;
    public String localId;
    public String displayName;
    public String idToken;
    public boolean registered;
    public String refreshToken;
    public String expiresIn;

    // Used only in unsuccessful attempts
    public Error error;
}
