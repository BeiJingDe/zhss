package com.ylxx.cloud.upms.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * @Auther: hanying
 * @Date: 2019/3/13 14:07
 * @Description:
 */
public class Base64Util {
    static final Base64.Decoder decoder = Base64.getDecoder();
    static final Base64.Encoder encoder = Base64.getEncoder();

    public Base64Util() {
    }

    public static String encode(String message) throws UnsupportedEncodingException {
        byte[] textByte = message.getBytes("UTF-8");
        return encoder.encodeToString(textByte);
    }

    public static String decode(String message) throws UnsupportedEncodingException {
        return new String(decoder.decode(message), "UTF-8");
    }

    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(isBase64("NIX4OpiAGulw"));
        System.out.println(decode("ZXZlcnNpZ2h0XzEyM0BA"));
        System.out.println(decode("U0hETEAxMjM="));
        System.out.println(encode("SHDL@123"));
    }

}
