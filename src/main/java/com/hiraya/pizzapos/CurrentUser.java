package com.hiraya.pizzapos;

import java.util.ArrayList;

import com.hiraya.pizzapos.productSettings.Product;

public class CurrentUser {
    private boolean isLoggedIn = false;
    private String accessToken;
    private String idToken;
    private String refreshToken;
    private String localId;
    public String profilePic = App.class.getResource("images/userProfilepic.png").toExternalForm();
    public String displayName = "";

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public String getLocalId() {
        return localId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getIdToken() {
        return idToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }

    // This method is a wrapper for setting the credentials and logging in
    // a user on our end
    public void setTokens(String refreshToken, String idToken, String accessToken, String localId) {
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.isLoggedIn = true;
        this.localId = localId;
    }

    // This method is a wrapper for deleting the local credentials of the user
    // effectively logging them out
    public void logout() {
        this.accessToken = null;
        this.refreshToken = null;
        this.idToken = null;
        this.isLoggedIn = false;
        this.localId = null;
    }

    public String name;
    // TODO: Add Transactions, etc etc
    public ArrayList<Product> products;
}
