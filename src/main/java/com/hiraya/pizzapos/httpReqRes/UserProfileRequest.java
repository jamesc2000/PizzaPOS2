package com.hiraya.pizzapos.httpReqRes;

import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileRequest extends BaseRequest {
    public String idToken;
    public String displayName;
    public String photoUrl;
    public ArrayList<String> deleteAttribute;
    // Secure token will be given to us on login,
    // but I put this here so we can override it if needed
    public boolean returnSecureToken = false;

    public UserProfileRequest(
        String idToken,
        String firstName,
        String lastName,
        URL photoUrl
    ) {
        this.displayName = firstName + " " + lastName;
        this.idToken = idToken;
        if (photoUrl != null) {
            this.photoUrl = photoUrl.toString();
        }
    }
}
