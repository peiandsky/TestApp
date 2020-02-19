package com.zlcdg.libcommon.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * SHA1和SHA256加密
 *
 * @author wangpeng
 * @date 2018-07-17
 */
public class HmacSHA {

    private static final String ENCODING = "UTF-8";

    public static String HMACSHA1(String encryptText, String encryptKey) {
        try {
            byte[] data = encryptKey.getBytes(ENCODING);
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);

            byte[] text = encryptText.getBytes(ENCODING);
            return byte2hex(mac.doFinal(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String HMACSHA256(String encryptText, String encryptKey) {
        try {
            byte[] data = encryptText.getBytes(ENCODING);
            byte[] key = encryptKey.getBytes(ENCODING);
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
}
