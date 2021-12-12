package com.hiraya.pizzapos.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiraya.pizzapos.httpReqRes.*;
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
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();
    
    // ==== LOGIN ====
    public static LoginResponse login(String t_email, String t_password) throws InterruptedException, IOException {
        LoginRequest credentials = new LoginRequest();
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
        return jsonToLoginRes(res.body().toString());
    }

    // Just a function to tidy up the login function
    private static LoginResponse jsonToLoginRes(String json) {
        LoginResponse res = new LoginResponse();
        
        try {
            res = mapper.readValue(json, LoginResponse.class);
        } catch (IOException e) {
            System.out.println(e);
        }

        return res;
    }

    // ==== REGISTER ====
    public static FirebaseAuthRegisterResponse register(FirebaseAuthRegisterRequest model) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signUp" + API_KEY))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(model.toJson()))
            .build();

        HttpResponse<String> res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body());

        return jsonToFbAuthRes(res.body().toString());
    }

    private static FirebaseAuthRegisterResponse jsonToFbAuthRes(String json) {
        FirebaseAuthRegisterResponse res = new FirebaseAuthRegisterResponse();

        try {
            res = mapper.readValue(json, FirebaseAuthRegisterResponse.class);
        } catch (IOException e) {
            System.out.println(e);
        }

        return res;
    }

    // ==== REGISTER PART 2 (Update User Profile) ====
    public static UserProfileResponse updateProfile(UserProfileRequest profile) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:update" + API_KEY))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(profile.toJson()))
            .build();

        HttpResponse<String> res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body());

        return jsonToProfileRes(res.body().toString());
    }

    private static UserProfileResponse jsonToProfileRes(String json) {
        UserProfileResponse res = new UserProfileResponse();

        try {
            res = mapper.readValue(json, UserProfileResponse.class);
        } catch (IOException e) {
            System.out.println(e);
        }

        return res;
    }

    // ==== REFRESH TOKEN ====
    public static SendRefreshTokenResponse sendRToken(SendRefreshTokenRequest token) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://securetoken.googleapis.com/v1/token" + API_KEY))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(token.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());

        return jsonToSRTokenres(res.body().toString());
    }
    private static SendRefreshTokenResponse jsonToSRTokenres(String json) {
        SendRefreshTokenResponse res = new SendRefreshTokenResponse();

        try {
            res = mapper.readValue(json, SendRefreshTokenResponse.class);
        } catch (IOException e) {
            System.out.println(e);
        }

        return res;
    }

    // ==== FIRESTORE ====
    // TODO: This should be FirestoreResponse
    public static void createProduct(AddProductFields fields, String idToken) throws IOException, InterruptedException {
        FirestoreRequest<AddProductFields> body = new FirestoreRequest<AddProductFields>(fields);
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1/projects/pizzapos-41338/databases/(default)/documents/products/"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .POST(BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        // return jsonToSRTokenres(res.body().toString());
    }
}
