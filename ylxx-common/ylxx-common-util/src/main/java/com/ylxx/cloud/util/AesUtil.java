package com.ylxx.cloud.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Aes加密
 *
 * @author
 */
@Slf4j
public class AesUtil {
    //private static final String KEY_AES = "AES";
    //private static final String KEY1 = "ca3d5c70b05beb796ec8bdb0296df96e";
    private static final String AES = "ca3d5c70b05beb79";
    private static final String IV = "6ec8bdb0296df96e";

    /*public static String encrypt2(String src) {
        byte[] encrypted = null;
        try {
            byte[] raw = KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(src.getBytes("utf-8"));

        } catch (Exception e) {
            log.error("AES加密失败 : {}", e.getMessage());

        }
        return byte2hex(encrypted);
    }

    public static String decrypt2(String src) {
        String originalString = null;
        try {
            byte[] raw = KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(src);
            byte[] original = cipher.doFinal(encrypted1);
            originalString = new String(original, "UTF-8");

        } catch (Exception e) {
            log.error("AES解密失败 : {}", e.getMessage());
        }
        return originalString;
    }*/

    private static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    /**
     * @param data 明文
     * @return 密文
     * @author miracle.qu
     * @Description AES算法加密明文
     */
    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;

            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(AES.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());  // CBC模式，需要一个向量iv，可增加加密算法的强度

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return byte2hex(encrypted);
        } catch (Exception e) {
            log.error("AES加密失败 : {}", e.getMessage());
            return null;
        }
    }

    /**
     * @param data 密文
     * @return 明文
     * @author miracle.qu
     * @Description AES算法解密密文
     */
    public static String decrypt(String data) {
        try {
            byte[] encrypted1 = hex2byte(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(AES.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            log.error("AES解密失败 : {}", e.getMessage());
            return null;
        }
    }

    /*public static void main(String[] args) {
        String decrypt_str = decrypt("6A3318DC8F6F15869501E095255A316CA1C1B04D8C20D934F3F338BBE294156E9FC0516EC5B7EED1CA251B88BA17E78E");
        String encrypt_str = encrypt("{\"reportId\": 60\"}");
    }*/

}