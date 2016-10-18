package com.yeepay.yop.library.oauth2;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/10/11 下午5:27
 */
public class OAuth2Client {

    private static String serverUrl="";

    /**
     * OAuth2授权过程
     * @param methodUrl
     * @param username
     * @param password
     * @return
     */
    public static Boolean getAuthenCredentials(String methodUrl,String username,String password){
        OkHttpClient okHttpClient=new OkHttpClient();
        Response response=null;
        Boolean result=false;
        Gson json=new Gson();
        RequestBody requestBody=new FormBody.Builder().add("username",username).add("password",password)
                .add("grant_type","password").add("scope","test").build();
        Request request=new Request.Builder().url(serverUrl+methodUrl).post(requestBody).build();
        try {
            response=okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!(response==null)&&response.isSuccessful()){
            result=true;
            String responseResult = response.body().toString();
            Authen authen=json.fromJson(responseResult,Authen.class);
        }
        return result;
    }

    /**
     * 使用access_token鉴权后台的接口
     * @param methodUrl
     * @param access_token
     * @return
     */
    public static String oauthCertificate(String methodUrl,String access_token){
        OkHttpClient okHttpClient=new OkHttpClient();
        Response response=null;
        String responseResult=null;
        RequestBody requestBody=new FormBody.Builder().add("access_token",access_token).build();
        Request request=new Request.Builder().url(serverUrl+methodUrl).post(requestBody).build();
        try {
            response=okHttpClient.newCall(request).execute();
            if(!(response==null)&&response.isSuccessful()){
                responseResult = response.body().toString();
            }else{
                throw new IOException(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    public static String refreshApi(String methodUrl,String refresh_token){
        OkHttpClient okHttpClient=new OkHttpClient();
        Response response=null;
        String responseResult=null;
        RequestBody requestBody=new FormBody.Builder().add("refresh_token",refresh_token).add("grant_type","refresh_token").build();
        Request request=new Request.Builder().url(serverUrl+methodUrl).post(requestBody).build();
        try {
            response=okHttpClient.newCall(request).execute();
            if(!(response==null)&&response.isSuccessful()){
                responseResult = response.body().toString();
            }else{
                throw new IOException(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }

}
