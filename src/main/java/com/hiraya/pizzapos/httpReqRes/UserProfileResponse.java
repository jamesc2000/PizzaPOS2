package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
// Ignoring kind, passwordHash, and providerUserInfo properties since we
// dont use them anyway
public class UserProfileResponse extends BaseResponse {
    public String localId;
    public String email;
    public String displayName;
    public String photoUrl;
    public String expiresIn;
    public boolean emailVerified;
}
