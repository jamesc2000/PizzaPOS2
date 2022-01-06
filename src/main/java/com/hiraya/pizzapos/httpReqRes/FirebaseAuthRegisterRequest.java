package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FirebaseAuthRegisterRequest extends BaseRequest {
    public String idToken; // Not used in register, but used in changepass to save classes
    public String email;
    public String password;
    private final boolean returnSecureToken = true;
}
