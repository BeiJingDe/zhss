package com.ylxx.cloud.upms.util;


import cn.hutool.core.util.IdUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加解密工具类
 *
 * @author xgshi@abcft.com
 * @version 0.0.1-SNAPSHOT
 * @since 2019/7/31
 **/
public abstract class MD5Utils {

    /**
     * 自注册用户密码MD5加密
     *
     * @param plainText 待加密串
     * @return 加密结果
     */
    public static String getMd5(String plainText) {
        // TODO 非标准
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;

            StringBuilder buf = new StringBuilder();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        System.out.println(MD5Utils.getMd5("NIX4OpiAGulw"));
        System.out.println(MD5Utils.getMd5("1234qwer"));
        System.out.println(IdUtil.fastSimpleUUID());
    }
}
