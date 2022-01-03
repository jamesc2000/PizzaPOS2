package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UpdateEmailRequest extends BaseRequest {
    public String idToken;
    public String email;
    public final Boolean returnSecureToken = false;

    public UpdateEmailRequest() {}

    public UpdateEmailRequest(
        String idToken,
        String newEmail
    ) {
        this.idToken = idToken;
        this.email = newEmail;
    }
}
