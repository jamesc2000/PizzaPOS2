package com.hiraya.pizzapos.httpReqRes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SendRefreshTokenResponse extends BaseResponse{
    public String expires_in;
    public String token_type;
    public String refresh_token;
    public String id_token;
    public String user_id;
    public String project_id;
}
