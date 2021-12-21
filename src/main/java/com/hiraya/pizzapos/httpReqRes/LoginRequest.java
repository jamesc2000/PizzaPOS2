package com.hiraya.pizzapos.httpReqRes;

public class LoginRequest extends BaseRequest {
    public String email;
    public String password;
    public final boolean returnSecureToken = true; // This is always true anyway, so let's just hard code it
}
