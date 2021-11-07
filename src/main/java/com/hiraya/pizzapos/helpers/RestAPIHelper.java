package com.hiraya.pizzapos.helpers;

import java.net.URI;
import java.time.Duration;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;


public class RestAPIHelper {
    private static final String API_KEY = "?key=AIzaSyAjHvrGH9RaL-kmgc7MyULmRFJMqmfcohU";
    
    public static LoginData.LoginResponse login(String t_email, String t_password) throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        LoginData data = new LoginData();

        LoginData.LoginBody credentials = data.new LoginBody();
        credentials.email = t_email; // Take the email parameter and store it in the credentials object
        credentials.password = t_password; // Same as above but for password;
        
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword" + API_KEY))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(credentials.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body());
        System.out.println("login() executed");
        return LoginData.jsonResToObj(res.body().toString());
    }
}
