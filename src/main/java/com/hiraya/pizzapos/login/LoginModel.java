/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hiraya.pizzapos.login;

import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.LoginResponse;
import java.io.IOException;

// Models contain the data representation of the things in the view
// For login, we have email, and password
public class LoginModel {
    private String email;
    private String password;

    public void setEmail(String t_email) {
        this.email = t_email;
    }

    public void setPassword(String t_password) {
        this.password = t_password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void sendToFirebase() {
        try {
            LoginResponse login = RestAPIHelper.login(this.email, this.password);
            System.out.println(login.email);
            System.out.println(login.idToken);
        } catch (InterruptedException | IOException e) {
            System.out.println(e);
        }
    }
}
