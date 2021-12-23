package com.hiraya.pizzapos.httpReqRes;

public class SendRefreshTokenRequest extends BaseRequest{
    public final String grant_type = "refresh_token"; //should always be refresh_token 
    public String refresh_token;
    
}
