package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;

@JsonInclude(Include.NON_NULL)
public class FirebaseAuthRegisterResponse {
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

    public String idToken;
    public String email;
    public String refreshToken;
    public String expiresIn;
    public String localId;
    public String kind;

    public Error error;
}
