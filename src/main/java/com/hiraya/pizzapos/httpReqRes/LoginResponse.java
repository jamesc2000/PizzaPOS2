package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LoginResponse extends BaseResponse {
    public String email;
    public String kind;
    public String localId;
    public String displayName;
    public String idToken;
    public boolean registered;
    public String refreshToken;
    public String expiresIn;
}
