package com.zlcdg.libcommon.security;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * MD5加密
 *
 * @author wangpeng
 * @date 2018-07-17
 */
public class MD5 {
    /**
     * 字符串MD5编码
     *
     * @param str 编码的字符串
     * @return
     */
    public static String md5(String str) {
        Charset charset = Charset.forName("utf-8");

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        byte[] md5Bytes = md5.digest(str.getBytes(charset));
        BigInteger b = new BigInteger(1, md5Bytes);
        return b.toString(16);
    }

    /**
     * 字符串MD5编码
     *
     * @return
     */
    public static String md5(byte[] bytes) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        byte[] md5Bytes = md5.digest(bytes);
        BigInteger b = new BigInteger(1, md5Bytes);
        return b.toString(16);
    }

    public static boolean verify(String content, String sign) {
        //String signData = content + key;
        String mySign = md5(content);
        boolean verify = false;
        if (mySign.equals(sign)) {
            verify = true;
        }
        return verify;
    }
}
