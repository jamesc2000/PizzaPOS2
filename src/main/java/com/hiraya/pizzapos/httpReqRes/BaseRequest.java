package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseRequest {
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
