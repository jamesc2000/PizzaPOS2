package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirebaseAuthRegisterResponse extends BaseResponse {
    public String idToken;
    public String email;
    public String refreshToken;
    public String expiresIn;
    public String localId;
    public String kind;
}
