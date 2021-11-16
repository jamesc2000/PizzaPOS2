package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FirebaseAuthRegisterRequest {
    public String email;
    public String password;
    private final boolean returnSecureToken = true;

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        
        String json = new String();
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println("Error in converting into json" + e.getMessage());
        }
        
        return json;
    }
}
