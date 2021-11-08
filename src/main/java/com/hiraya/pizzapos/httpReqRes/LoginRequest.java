package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginRequest {
    public String email;
    public String password;
    private final boolean returnSecureToken = true; // This is always true anyway, so let's just hard code it

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
