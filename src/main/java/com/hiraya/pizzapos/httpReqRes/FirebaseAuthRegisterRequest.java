package com.hiraya.pizzapos.httpReqRes;

public class FirebaseAuthRegisterRequest extends BaseRequest {
    public String email;
    public String password;
    private final boolean returnSecureToken = true;
}
