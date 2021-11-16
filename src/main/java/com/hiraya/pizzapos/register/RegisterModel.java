
package com.hiraya.pizzapos.register;

import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.FirebaseAuthRegisterRequest;
import com.hiraya.pizzapos.httpReqRes.FirebaseAuthRegisterResponse;
import java.io.IOException;

public class RegisterModel {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    // Add more fields here if more is required by the view

    public String getEmail() {
        return email;
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

    public void sendToFirebase() {
        System.out.println("sendToFirebase executed");
        try {
            FirebaseAuthRegisterRequest req = new FirebaseAuthRegisterRequest();
            req.email = this.email;
            req.password = this.password;
            FirebaseAuthRegisterResponse register;
            register = RestAPIHelper.register(req);
            System.out.println(register.email);
            System.out.println(register.idToken);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
