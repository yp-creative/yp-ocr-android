package com.yeepay.yop.library.client;

import android.webkit.MimeTypeMap;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.yeepay.yop.library.encrypt.AESEncrypt;
import com.yeepay.yop.library.encrypt.BlowfishEncrypter;
import com.yeepay.yop.library.encrypt.Digest;
import com.yeepay.yop.library.utils.UuidUtils;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
 * @since 16/9/14 下午4:19
 */
public class Yopclient {

    private static Boolean encrypt;
    private static String signName;
    private static String appKey;
    private static String secretKey;
    private static String customerNo;
    private static String encryptParams;
    private static String signParams;

    public static Boolean getSignRet() {
        return signRet;
    }

    public static void setSignRet(Boolean signRet) {
        Yopclient.signRet = signRet;
    }

    private static Boolean signRet;
    private static String serverUrl;

    private static String postUrl;

    public static String getPostUrl() {
        return postUrl;
    }

    public static void setPostUrl(String postUrl) {
        Yopclient.postUrl = postUrl;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        Yopclient.serverUrl = serverUrl;
    }

    public static String getCustomerNo() {
        return customerNo;
    }

    public static void setCustomerNo(String customerNo) {
        Yopclient.customerNo = customerNo;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        Yopclient.appKey = appKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public  static void setSecretKey(String secretKey) {
        Yopclient.secretKey = secretKey;
    }

    public static Boolean getEncrypt() {
        return encrypt;
    }

    public static void setEncrypt(Boolean encrypt) {
        Yopclient.encrypt = encrypt;
    }

    public static String getSignName() {
        return signName;
    }

    public static void setSignName(String signName) {
        Yopclient.signName = signName;
    }

    private static HashMap<String,String>map=new HashMap<String, String>();
    private static HashMap<String,String> fileParamMap=new HashMap<String, String>();
    private static HashMap<String,String>ignoreParams=new HashMap<String, String>();
    public static String postData(String methodUrl, Map params){
        signAndEncrypt(params);
        String version = StringUtils.substringBetween(methodUrl,
                "/rest/v", "/");
        HashMap<String,String>postMap=new HashMap<String, String>();
        postMap.put(YopConstants.TIMESTAMP,map.get(YopConstants.TIMESTAMP));
        postMap.put(YopConstants.LOCALE,"zh_CN");
        postMap.put(YopConstants.FORMAT,"json");
        postMap.put(YopConstants.VERSION,version);
        postMap.put("sign",signParams);
        if(getEncrypt()){
            postMap.put("encrypt",encryptParams);
        }else {
            postMap.putAll(params);
        }
        if(ignoreParams!=null&&ignoreParams.size()>0){
            postMap.putAll(ignoreParams);
        }
        if(!(appKey==null)){
            postMap.put(YopConstants.APP_KEY,getAppKey());}
        if(!(customerNo==null)){
            postMap.put(YopConstants.CUSTOMER_NO,getCustomerNo());
        }
        List<String>paramsName=new ArrayList<String>();
        RequestBody requestBody=null;
        String result=null;
        String responseResult=null;
        paramsName.addAll(postMap.keySet());
        FormBody.Builder builder=new FormBody.Builder();
        for(String param:paramsName){
            builder.add(param,postMap.get(param));
        }
        requestBody=builder.build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request=new Request.Builder().header("X-YOP-Request-ID", UuidUtils.getUUID()).url(serverUrl+methodUrl).post(requestBody).build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            responseResult=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(getEncrypt()){
            result=decryptParams(responseResult);
            return result;
        }
         return responseResult;
    }

    public static String uploadFile(String uploadUrl,Map<String,String> businessMap){
        signAndEncrypt(businessMap);
        String version = StringUtils.substringBetween(uploadUrl,
                "/rest/v", "/");
        HashMap<String,String>postMap=new HashMap<String, String>();
        postMap.put(YopConstants.TIMESTAMP,map.get(YopConstants.TIMESTAMP));
        postMap.put(YopConstants.LOCALE,"zh_CN");
        postMap.put(YopConstants.FORMAT,"json");
        postMap.put(YopConstants.VERSION,version);
        postMap.put("sign",signParams);
        if(getEncrypt()){
            postMap.put("encrypt",encryptParams);
        }else {
            postMap.putAll(businessMap);
        }
        if(!(appKey==null)){
            postMap.put(YopConstants.APP_KEY,getAppKey());}
        if(!(customerNo==null)){
            postMap.put(YopConstants.CUSTOMER_NO,getCustomerNo());
        }
        List<String>paramsName=new ArrayList<String>();
        RequestBody requestBody=null;
        String result=null;
        String responseResult=null;
        paramsName.addAll(postMap.keySet());
        MultipartBody.Builder builder=new MultipartBody.Builder();
        for(String param:paramsName){
            builder.addFormDataPart(param,postMap.get(param));
        }
        if(fileParamMap.containsKey("_file")){
            builder.addFormDataPart("_file","_file",RequestBody.create(MediaType.parse("application/octet-stream"), new File(fileParamMap.get("_file"))));
            requestBody=builder.build();
            OkHttpClient okHttpClient = new OkHttpClient();

            Request request=new Request.Builder().header("X-YOP-Request-ID", UuidUtils.getUUID()).url(serverUrl+uploadUrl).post(requestBody).build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                responseResult=response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(getEncrypt()){
                result=decryptParams(responseResult);
                return result;
            }
        }else {
            System.out.println("_file参数不能为空！");
        }
        return responseResult;

    }

    /**
     * 对参数进行签名和加密
     * @param params
     */
    public static void signAndEncrypt(Map<String,String> params){
        if(appKey==null&&customerNo==null){
            System.out.println("appKey和customerNo不能同时为空");
        }
        if(!(appKey==null)){
            map.put(YopConstants.APP_KEY,getAppKey());
        }
        if(!(customerNo==null)){
            map.put(YopConstants.CUSTOMER_NO,getCustomerNo());
        }
        Iterator<String>it=params.keySet().iterator();
        while(it.hasNext()){
            String key=it.next();
            if(YopConstants.isProtectedKey(key)){
                ignoreParams.put(key,params.get(key));
                it.remove();
            }
        }
        if(params.containsKey("_file")){
            fileParamMap.put("_file",params.get("_file"));
            params.remove("_file");
        }
        String version = StringUtils.substringBetween(getPostUrl(),
                "/rest/v", "/");
        map.put(YopConstants.TIMESTAMP,String.valueOf(System.currentTimeMillis()));
        map.put(YopConstants.LOCALE,"zh_CN");
        map.put(YopConstants.FORMAT,"json");
        map.put(YopConstants.VERSION,version);
        map.put(YopConstants.METHOD,getPostUrl());
        map.putAll(params);
        if(getSignRet()){
            signParams=sign(map,getSignName());
        }else {
            signParams="";
        }
        if(getEncrypt()){
            encryptParams=encryptParams(params);
        }else{
            encryptParams="";
        }
    }

    /**
     * 对参数进行签名
     * @return
     */
    public static String sign(Map <String,String>signParams,String algName){
        if(algName.isEmpty()){
            algName="SHA-256";
        }
        StringBuilder builder=new StringBuilder();
        List<String>paramNames=new ArrayList<String>(signParams.size());
        paramNames.addAll(signParams.keySet());
//        for(String ignoreParam:paramNames){
//            if(YopConstants.isProtectedKey(ignoreParam)){
//                ignoreParams.put(ignoreParam,signParams.get(ignoreParam));
//                signParams.remove(ignoreParam);
//            }
//        }
        Collections.sort(paramNames);
        builder.append(Yopclient.getSecretKey());
        for(String paramName:paramNames){
            try {
//                builder.append(paramName).append(URLEncoder.encode(signParams.get(paramName),YopConstants.ENCODING));
                builder.append(paramName).append(signParams.get(paramName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        builder.append(Yopclient.getSecretKey());
        return Digest.digest(builder.toString(),algName);
    }

    /**
     * 对参数进行加密，APPKey形式的进行AES加密，customerNo形式进行blowfish加密
     * @param Params
     * @return
     */
    public static String encryptParams(Map <String,String>Params){
        List<String>keys=new ArrayList<String>(Params.size());
        Map<String,String>encryptParams=new TreeMap<String, String>();
        String encryptBody=null;
        String encrypt=null;
        keys.addAll(Params.keySet());
        Collections.sort(keys);
        for(String key:keys){
            try {
                encryptParams.put(key,URLEncoder.encode(Params.get(key), YopConstants.ENCODING));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        encryptBody = Joiner.on("&").withKeyValueSeparator("=").join(encryptParams);
        if(!(appKey==null)){
            encrypt= AESEncrypt.encrypt(encryptBody,getSecretKey());
        }
        else if(!(customerNo==null)){
            encrypt= BlowfishEncrypter.encrypt(encryptBody,getSecretKey());
        }
        return encrypt;
    }

    /**
     * 对加密的内容进行解密，appkey对应AES解密，customerNo对应Blowfish解密
     * @param encryptString
     * @return
     */
    public static String decryptParams(String encryptString){
        String decryptResult=null;
        StringBuilder stringBuilder=new StringBuilder();
        StringBuilder stringBuilderError=new StringBuilder();
        try {
            JSONObject jsonObject=new JSONObject(encryptString);
            String state=(String)jsonObject.get("state");
            String ts=String.valueOf(jsonObject.get("ts"));
            if(state.equals("SUCCESS")){
                String encryptResult=(String)jsonObject.get("result");
                if(!(appKey==null)){
                    decryptResult=AESEncrypt.decrypt(encryptResult,getSecretKey());
                }else if(!(customerNo==null)){
                    decryptResult=BlowfishEncrypter.decrypt(encryptResult,getSecretKey());
                }
                stringBuilder.append("state:").append(state).append("\n").append("result:").append(decryptResult)
                        .append("\n").append("ts:").append(ts);

            }else if(state.equals("FAILURE")){
                return encryptString;
            }
                    } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String sign(Map<String, String> paramValues, List<String> ignoreParamNames, String secret, String algName) {
//        Assert.notNull(paramValues);
//        Assert.notNull(secret);
        if (algName == null || algName.isEmpty()) {
            algName = "SHA1";
        }

        StringBuilder sb = new StringBuilder();
        ArrayList paramNames = new ArrayList(paramValues.size());
        paramNames.addAll(paramValues.keySet());
        Iterator i$;
        String paramName;
        if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
            i$ = ignoreParamNames.iterator();

            while (i$.hasNext()) {
                paramName = (String) i$.next();
                paramNames.remove(paramName);
            }
        }

        Collections.sort(paramNames);
        sb.append(secret);
        i$ = paramNames.iterator();

        while (i$.hasNext()) {
            paramName = (String) i$.next();
            String paramval = paramValues.get(paramName);
            if (paramval != null && !paramval.isEmpty()) {
                sb.append(paramName).append(paramval);
            }
        }

        sb.append(secret);
        return Digest.digest(sb.toString(), algName);
    }


}
