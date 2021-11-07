package com.hiraya.pizzapos.helpers;

import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.io.IOException;
import java.util.ArrayList;

public class LoginData {
    public class LoginBody {
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

    public static class LoginResponse {
        public static class ErrorListElem {
            public String message;
            public String domain;
            public String reason;
        }

        public static class Error {
            public String code;
            public String message;
            public ArrayList<ErrorListElem> errors;
        }
        
        // Used only in successful attempts
        public String email;
        public String kind;
        public String localId;
        public String displayName;
        public String idToken;
        public boolean registered;
        public String refreshToken;
        public String expiresIn;

        // Used only in unsuccessful attempts
        public Error error;
    }

    public static LoginResponse jsonResToObj(String res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        LoginResponse outputObj;

        outputObj = mapper.readValue(res, LoginResponse.class);

        return outputObj;
    }
}
    
