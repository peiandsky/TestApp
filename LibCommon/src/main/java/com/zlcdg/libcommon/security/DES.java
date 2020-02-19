package com.zlcdg.libcommon.security;


import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加密解密
 *
 * @author wangpeng
 * @date 2018-07-17
 */
public class DES {
    /**
     * DES算法密钥
     */
//    private static final byte[] DES_KEY = {20, 11, -114, 82, -32, -89, -128, -65};

    private static String KEY = "bkFmk$I9KnmYys1y";
    /**
     * 数据加密
     *
     * @param data 要进行加密的数据
     * @return 加密后的数据
     */
    public static String encode(String data) {
        String encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            // 加密，并把字节数组编码成字符
            encryptedData = Base64.encodeBytes(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    public static String encode(String data, String key) {
        String encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 加密，并把字节数组编码成字符
            encryptedData = Base64.encodeBytes(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * 数据解密
     *
     * @param cryptData 加密数据
     * @return 解密后的数据
     */
    public static String decode(String cryptData) {
        String decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(KEY.getBytes());
            // 创建个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            // 把字符串解码为字节数组，并解
            decryptedData = new String(cipher.doFinal(Base64.decode(cryptData)), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：" + e.getMessage(), e);
        }
        return decryptedData;
    }

    public static String decode(String cryptData, String key) {
        String decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
            // 创建个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 把字符串解码为字节数组，并解
            decryptedData = new String(cipher.doFinal(Base64.decode(cryptData)),Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：" + e.getMessage(), e);
        }
        return decryptedData;
    }

    public static byte[] decrypt(String cryptData, String key) {
        try {
            int len = cryptData.length() / 2;
            byte[] target = new byte[len];
            int x, i;
            for (x = 0; x < len; x++) {
                i = Integer.parseInt(cryptData.substring(x * 2, ((x + 1) * 2)), 16);
                target[x] = (byte) i;
            }
            key = MD5.md5(key).toUpperCase().substring(0, 8);
            byte[] ivs = key.getBytes("UTF-8");
            if (ivs != null) {

            }
            DESKeySpec desKeySpec = new DESKeySpec(ivs);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            IvParameterSpec iv = new IvParameterSpec(ivs);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(cipher.update(target));
            bos.write(cipher.doFinal());
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(URLEncoder.encode(encode("123456"),"UTF-8"));
        System.out.println(decode(URLDecoder.decode("Jl36A4ASfZU%3D","UTF-8")));
        //System.out.println(decryption("941EE126C822595477A0A4691BA5B3B5F4489415F4572DCEDD7F79685E85DA0993F5D24D5DB178D360B191D82F041BFCD3C83335B990AAEB4D202E38B59D239E850B2F4A63D55DC42E2A48F2FFA8D1E508AD87611AC10ACEFF9DF16247D513430CCC8F327729547AE4E54DCDE6CC9C93BFC558C90C5CFE926B253FD1BFEBEA9CBAB7DF3171D8BB60675F3A4752487304FDAD6AEDD91E419B03A650EBA9B86DAEFD03E5B3808384220E90CEDE9A6C481BCA90164C1331D861D189E4320281F4ACA3795F59819349D14071A4E26CDC1F518D71833C8C6A0F4E1892692F5E2417670F9DFD1640A93ABB36D4EFA1611970BDE29E4AC31167C7C7D5E9DDE6738516DC28E4C3FE6C2C0B1407345574A0B180859844D989C52115FD1F92E6B5BC9715469A727FDC05EA82FDBE38F2306B3C055FBD8066A29079C5E918B0BF048C49F605276B4AD70544138C209736F2D20AFD524D244BE53E4DEE41A831F8B4FFD501940E857F20BE69444604C07EF896AD97F50F56C52B5E0CADE9239DF6E521CA5B1D95F4B50654C08242BCFA88A1EA9182B6A36993DF72BFA3E71D7BA8221785886251B53F17025A57F9FAD98BAD98E76237F73533AC94F786051D24D79CBFBE58113480FD90ED0AD6B74E507913852DC8312ADA92D62F3A564A5F6582D80C634E07C0DD8E57A66566A81C4CFE7F30BB22F6B5FDB876545680B903CE47C3D4DC43733198BD51AA49104D152A53D17D5CB910E3B30B7C7182CE2C0A3B9CA95F418A3EDFFC221B2577735C43045EAD610D03CF0B67D8CE3D1CB39AB61CD3AB44537BAC6D257EBC820988F1ABA20EF59F38B72E158DCB6BAB23E93FDBAD3C6FCB5A5E9F0C96C79C331020BAAD0202AE3BDD1DBA7C789101CAD3ABA2809E8D1336CE27ABB5CE9B2FED7041F9DA7ACCE5240AF0F784D15391070546906D78CEBE8091CD77917BE2571259E89F8B824ABB43933EA2E9ACC52CE9AD96A586EAFC4B111B786BD4FF667808D8A7D5CA9A2ADA9140B33599489AA0A4657633E225CBBE14A7C46407EB192FF196C3D73D7A78CDA6D03822E007A8D56D0525B3EE18CC4DC59221575B80D491D4E6D0C0", "27b893c5"));
        //System.out.println(encode("exceedDurationStart=201801&exceedDurationEnd=201808&evaluateDurationStart=201801&evaluateDurationEnd=201810&inDurationStart=201809&inDurationEnd=201810&clientNumbers=88010007,88010008,88010009","27b893c5"));
    }
}
