package com.zlcdg.libcommon.security;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA加密
 *
 * @author wangpeng
 * @date 2018-07-17
 */
public class RSA {

    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIwqH0VWCeolPgTNjZO1qaQvBDWXBNfBijwaqxkjH3GSIZEureF+LgmY/ivHCKEtYGiqviPa+ThfctJPUQ3e7+8zacbdOkqW7nfSVRdrPxsQiph1lCCWHT8fFPlGV0tIfkjhOlnn2CrXA/TbHxjdz2hnKaDn+hXafCcxJRS5QZFwIDAQAB";

    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIjCofRVYJ6iU+BM2Nk7WppC8ENZcE18GKPBqrGSMfcZIhkS6t4X4uCZj+K8cIoS1gaKq+I9r5OF9y0k9RDd7v7zNpxt06Spbud9JVF2s/GxCKmHWUIJYdPx8U+UZXS0h+SOE6WefYKtcD9NsfGN3PaGcpoOf6Fdp8JzElFLlBkXAgMBAAECgYArZgA45gMENCUDz78cqG4m98kAxlgR7qhvBt0g/nCmpi4g4NTxZ4kSlwiG/h+EYVfTuZuz7rlRyjhW9hxintrhRkBvp1zCXCTgoYCiGZlmTvTUcLyvcEqyVX6qq4DKWXcVly72nhTGKyBB4GmEiLSObOJYHd2dO6KroTSyDra4SQJBAP+PJEK1JMOEclrtneHujborb0J0RovZCsLGGfJJp1ZXeVb+yh0uCJ/QAqlaX8GrIUEg/yejyLtbl3DTl1TDREsCQQCI/wce6lXjGBRHMSU7SZIYh2OJKUXtDnJZ1VlUH1lijbx3dynOGrpF3/zSSdhaTm0m4Vrj1X5Krg/bCQ3MgsblAkEA93TAdMN4VMXUAU3iuhnHLITQV/XFNbc1H0K5bw14tjc/bEiMptKjUTQWz6uN2zb8nVb5GoYSYbEfpAWOnGnznwJAenKAGdjX9YfjMBK0NX63r2brx3/1eUHtYW/5TOBMU3NeHeWLnyVyktv2LLtGPslUdGj+hR+gpv1XPK6l/8JwJQJBANGRuDRYx4nZxCFWdjz2su2nXb7E7/B60I1f0XZnz/YsA/S2+ekm5fZkpIpEm1SBTfBkhxXEaiUj2qfgwu+wC9o=";

    private static Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> generateKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            String publicKeyString = getKeyString(publicKey);
            String privateKeyString = getKeyString(privateKey);
            Map<String, String> map = new HashMap<String, String>();
            map.put("publicKey", publicKeyString);
            map.put("privateKey", privateKeyString);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;

        keyBytes = Base64.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    private static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    private static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = Base64.encodeBytes(keyBytes);
        return s;
    }


    private static String encrypt(String publicKeyStr, String plainText) {
        try {
            PublicKey publicKey = getPublicKey(publicKeyStr);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBytes(enBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String decrypt(String privateKeyStr, String enStr) {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyStr);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] tempData = Base64.decode(enStr);
            byte[] deBytes = cipher.doFinal(tempData);
            return new String(deBytes, Charset.forName("UTF-8"));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String plainText) {
        return encrypt(PUBLIC_KEY, plainText);
    }

    public static String decrypt(String enStr) {
        return decrypt(PRIVATE_KEY, enStr);
    }

}
