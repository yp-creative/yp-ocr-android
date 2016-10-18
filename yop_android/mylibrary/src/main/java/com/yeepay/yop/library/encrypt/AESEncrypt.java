package com.yeepay.yop.library.encrypt;

import com.yeepay.yop.library.client.YopConstants;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/9/14 下午2:49
 */
public class AESEncrypt {
    /**
     * 加密
     *
     * @param data
     *            需要加密的内容
     * @param key
     *            加密密码
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
//        Assert.notNull(data, "data");
       // Assert.notNull(key, "key");
        if (key.length != 16) {
            throw new RuntimeException(
                    "Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key,
                    YopConstants.ALG_AES);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat,
                    YopConstants.ALG_AES);
            Cipher cipher = Cipher.getInstance(YopConstants.ALG_AES);// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, seckey);// 初始化
            byte[] result = cipher.doFinal(data);
            return result; // 加密
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    /**
     * 解密
     *
     * @param data
     *            待解密内容
     * @param key
     *            解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        //Assert.notNull(data, "data");
        //Assert.notNull(key, "key");
        if (key.length != 16) {
            throw new RuntimeException(
                    "Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key,
                    YopConstants.ALG_AES);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat,
                    YopConstants.ALG_AES);
            Cipher cipher = Cipher.getInstance(YopConstants.ALG_AES);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, seckey);// 初始化
            byte[] result = cipher.doFinal(data);
            return result; // 加密
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    public static String encrypt(String data, String key) {
        try {
            byte[] valueByte = encrypt(data.getBytes(YopConstants.ENCODING),
                    Base64.decode(key.getBytes(YopConstants.ENCODING)));
            return new String(Base64.encode(valueByte), YopConstants.ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    public static String decrypt(String data, String key) {
        try {
            byte[] originalData = Base64.decode(data.getBytes());
            byte[] valueByte = decrypt(originalData,
                    Base64.decode(key.getBytes(YopConstants.ENCODING)));
            return new String(valueByte, YopConstants.ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    public static String genarateRandomKey() {
        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance(YopConstants.ALG_AES);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(" genarateRandomKey fail!", e);
        }
        SecureRandom random = new SecureRandom();
        keygen.init(random);
        Key key = keygen.generateKey();
        return new String(Base64.encode(key.getEncoded()));
    }
}
