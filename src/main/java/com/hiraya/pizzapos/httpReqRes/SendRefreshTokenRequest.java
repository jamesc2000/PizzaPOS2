/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiraya.pizzapos.httpReqRes;

/**
 *
 * @author Alteisen
 */
public class SendRefreshTokenRequest extends BaseRequest{
    public final String grant_type = "refresh_token"; //should always be refresh_token 
    public String refresh_token;
    
}
