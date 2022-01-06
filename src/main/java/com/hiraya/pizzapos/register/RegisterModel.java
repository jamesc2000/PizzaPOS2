
package com.hiraya.pizzapos.register;

import java.io.IOException;
import java.net.URL;

import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.FirebaseAuthRegisterRequest;
import com.hiraya.pizzapos.httpReqRes.FirebaseAuthRegisterResponse;
import com.hiraya.pizzapos.httpReqRes.UserFields;
import com.hiraya.pizzapos.httpReqRes.UserProfileRequest;
import com.hiraya.pizzapos.httpReqRes.UserProfileResponse;

public class RegisterModel {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private URL imageUrl;
    // Add more fields here if more is required by the view

    public String getEmail() {
        return email;
    }

    public URL getImageUrl() {
        return imageUrl;
    }
    
    public String getSafeStringImage() {
        if (this.imageUrl == null) {
            return "";
        } else {
            return this.imageUrl.toExternalForm();
        }
    }

    public void setImageUrl(String imageUrl) {
        try {
            this.imageUrl = new URL(imageUrl);
        } catch (Exception e) {
            //TODO: handle exception
            this.imageUrl = null;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFields(
        String email,
        String password,
        String firstName,
        String lastName
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserProfileResponse sendToFirebase() {
        System.out.println("sendToFirebase executed");
        UserProfileResponse profile = new UserProfileResponse();
        try {
            FirebaseAuthRegisterRequest req = new FirebaseAuthRegisterRequest();
            req.email = this.email;
            req.password = this.password;
            FirebaseAuthRegisterResponse register = RestAPIHelper.register(req);

            if (register.isSuccessful()) {
                System.out.println(register.email);
                System.out.println(register.idToken);
                UserProfileRequest req2 = new UserProfileRequest(
                    register.idToken,
                    this.firstName,
                    this.lastName,
                    this.imageUrl
                );
                profile = RestAPIHelper.updateProfile(req2);
                System.out.println("displayName: " + profile.displayName);

                UserFields fields = new UserFields(profile.displayName, profile.email, "", this.getSafeStringImage(), profile.localId);
                RestAPIHelper.createUser(fields, register.idToken, register.localId);
            } else {
                profile.error = register.error;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return profile;
    }
}
