package com.hiraya.pizzapos.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.httpReqRes.*;
import com.hiraya.pizzapos.productSettings.Category;
import com.hiraya.pizzapos.productSettings.Product;
import com.hiraya.pizzapos.transactionHistory.Transaction;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
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

    // ==== Update Email ==== // FBAuthRegisterResponse only because I'm lazy, they're similar anyway
    public static UserProfileResponse updateEmail(UpdateEmailRequest profile) throws IOException, InterruptedException {
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

    // === Create User Document in Firestore ===
    public static void createUser(UserFields fields, String idToken, String newUserLocalId) throws IOException, InterruptedException {
        FirestoreRequest<UserFields> body = new FirestoreRequest<UserFields>(fields);
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1/projects/pizzapos-41338/databases/(default)/documents/users/?documentId=" + newUserLocalId))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .POST(BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        // return jsonToSRTokenres(res.body().toString());
    }

    // === Update User Document in Firestore ===
    public static void updateUser(UserFields fields, String idToken) throws IOException, InterruptedException {
        FirestoreRequest<UserFields> body = new FirestoreRequest<UserFields>(fields);
        body.name = App.user.getLocalId();
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1/projects/pizzapos-41338/databases/(default)/documents/users/" + App.user.getLocalId()))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .method("PATCH", BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        // return jsonToSRTokenres(res.body().toString());
    }

    public static UserFields getUserData() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1beta1/projects/pizzapos-41338/databases/(default)/documents/users/" + App.user.getLocalId()))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + App.user.getIdToken())
            .GET()
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        JsonNode fsReq = mapper.readTree(res.body().toString());
        // System.out.println("HEREEEE=====");
        // System.out.println(fsReq.get("fields").toString());
        UserFields fields = mapper.readValue(fsReq.get("fields").toString(), UserFields.class);
        // System.out.println("name: " + fields.name.stringValue);
        return fields;
    }

    // ==== CHANGE PASSWORD ====
    public static FirebaseAuthRegisterResponse changePassword(FirebaseAuthRegisterRequest body) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:update" + API_KEY))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse<String> res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body());

        return jsonToFbAuthRes(res.body().toString());
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
    
    public static ArrayList<Product> getProducts(String idToken) throws IOException, InterruptedException {
        RunQueryRequest body = new RunQueryRequest("products");
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1beta1/projects/pizzapos-41338/databases/(default)/documents:runQuery"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .POST(BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        return jsonToProducts(res.body().toString());
    }

    public static void updateProduct(AddProductFields fields, String idToken, String docId) throws IOException, InterruptedException {
        System.out.println("UPDATE PRODUCT==========================="+docId);
        FirestoreRequest<AddProductFields> body = new FirestoreRequest<>(fields);
        body.name = docId;
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1/projects/pizzapos-41338/databases/(default)/documents/products/" + docId))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .method("PATCH", BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        // return jsonToSRTokenres(res.body().toString());
    }

    public static void deleteProduct(String docId, String idToken) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1/projects/pizzapos-41338/databases/(default)/documents/products/" + docId))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .DELETE()
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println("Delete res: " + res.body().toString());
        if (res.body().toString().length() > 3) {
            System.out.println("VALUE OF RES: " + res.body().toString());
            System.out.println("Length: " + res.body().toString().length());
            throw new Exception("Error in deleting product");
        }
    }

    private static ArrayList<Product> jsonToProducts(String json) {
        GetProductsResponse[] res = {};
        ArrayList<Product> out = new ArrayList<Product>();

        try {
            res = mapper.readValue(json, GetProductsResponse[].class);
            if (res[0].document.name == null) return out; // Return early if retrieved firebase responded empty
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println(res[0].document);
        for (int i = 0; i < res.length; ++i) {
            Product temp = new Product();
            temp.setValues(
			    res[i].document.name,
			    res[i].document.fields.name.stringValue, 
			    res[i].document.fields.imageUrl.stringValue, 
			    res[i].document.fields.category.stringValue, 
			    new ArrayList<String>(),
			    new ArrayList<Double>()
			);
			res[i].document.fields.sizes.arrayValue.values.forEach(size -> {
			    temp.pushSize(size.stringValue);
			});
			res[i].document.fields.prices.arrayValue.values.forEach(price -> {
                if (price.doubleValue != null) {
                    temp.pushPrice(price.doubleValue);
                } else if (price.integerValue != null) {
                    temp.pushPrice(Double.valueOf(price.integerValue));
                }
			});
            out.add(temp);
        }
        return out;
    }

    public static void createCategory(NewCategoryFields fields, String idToken) throws IOException, InterruptedException {
        FirestoreRequest<NewCategoryFields> body = new FirestoreRequest<NewCategoryFields>(fields);
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1/projects/pizzapos-41338/databases/(default)/documents/categories/"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .POST(BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        // return jsonToSRTokenres(res.body().toString());
    }

    public static ArrayList<Category> getCategories(String idToken) throws IOException, InterruptedException {
        RunQueryRequest body = new RunQueryRequest("categories");
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1beta1/projects/pizzapos-41338/databases/(default)/documents:runQuery"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .POST(BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        return jsonToCategory(res.body().toString());
    }

    private static ArrayList<Category> jsonToCategory(String json) {
        GetCategoriesResponse[] res = {};
        ArrayList<Category> out = new ArrayList<Category>();

        try {
            res = mapper.readValue(json, GetCategoriesResponse[].class);
            if (res[0].document == null) return out; // Return early if retrieved firebase responded empty
        } catch (IOException e) {
            System.out.println(e);
        }
        // System.out.println("DOCUMENT" + res[0].document.fields.name.stringValue);
        for (int i = 0; i < res.length; ++i) {
            Category temp = new Category();
            if (!res[i].document.fields.name.stringValue.isEmpty()) {
                int idLength = res[i].document.name.split("/").length - 1;
                temp.id = res[i].document.name.split("/")[idLength];
                temp.name = res[i].document.fields.name.stringValue;
                try {
                    temp.imageUrl = new URL(res[i].document.fields.imageUrl.stringValue);
                } catch (MalformedURLException e) {
                    temp.imageUrl = App.class.getResource("images/addProduct.JPG");
                }
                out.add(temp);
            }
        }
        return out;
    }

    public static void deleteCategory(String docId, String idToken) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1/projects/pizzapos-41338/databases/(default)/documents/categories/" + docId))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .DELETE()
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println("Delete res: " + res.body().toString());
        if (res.body().toString().length() > 3) {
            System.out.println("VALUE OF RES: " + res.body().toString());
            System.out.println("Length: " + res.body().toString().length());
            throw new Exception("Error in deleting category");
        }
    }

    public static void createTransaction(NewTransactionFields fields, String idToken) throws IOException, InterruptedException {
        FirestoreRequest<NewTransactionFields> body = new FirestoreRequest<NewTransactionFields>(fields);
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1/projects/pizzapos-41338/databases/(default)/documents/transactions/"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .POST(BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        // return jsonToSRTokenres(res.body().toString());
    }

    public static ArrayList<Transaction> getTransactions(String idToken) throws IOException, InterruptedException {
        RunQueryRequest body = new RunQueryRequest("transactions");
        System.out.println("JSON Body Firestore: ");
        System.out.println(body.toJson());
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("https://firestore.googleapis.com/v1beta1/projects/pizzapos-41338/databases/(default)/documents:runQuery"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + idToken)
            .POST(BodyPublishers.ofString(body.toJson()))
            .build();

        HttpResponse res = client.send(req, BodyHandlers.ofString());
        System.out.println(res.body().toString());
        return jsonToTransactions(res.body().toString());
    }

    private static ArrayList<Transaction> jsonToTransactions(String json) {
        GetTransactionsResponse[] res = {};
        ArrayList<Transaction> out = new ArrayList<>();

        try {
            res = mapper.readValue(json, GetTransactionsResponse[].class);
            if (res[0].document.name == null) return out; // Return early if retrieved firebase responded empty
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println(res[0].document);
        for (int i = 0; i < res.length; ++i) {
            Transaction temp = new Transaction(res[i].document);
            out.add(temp);
        }
        return out;
    }
}
