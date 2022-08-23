package com.ylxx.cloud.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.sgitg.sgcc.sm.SM4Utils;

public class SM4Util {
	
	private static SM4Utils sm4Utils = new SM4Utils();
	
	/**
	 * 加密：byte类型
	 * @param keyBytes
	 * @param plainText
	 * @return
	 */
	public static byte[] encrypt(byte[] keyBytes, byte[] plainText) {
		if (keyBytes == null || keyBytes.length != 16) {
			throw new RuntimeException("key的byte数组不能为空且长度必须为16。");
		}
		if (plainText == null || plainText.length == 0) {
			throw new RuntimeException("明文的byte数组不能为空且长度必须为大于0。");
		}
		return sm4Utils.SG_EncECBData(keyBytes, plainText);
	}
	/**
	 * 解密：byte类型
	 * @param keyBytes
	 * @param cipherText
	 * @return
	 */
	public static byte[] decrypt(byte[] keyBytes, byte[] cipherText) {
		if (keyBytes == null || keyBytes.length != 16) {
			throw new RuntimeException("key的byte数组不能为空且长度必须为16。");
		}
		if(cipherText == null || cipherText.length == 0 || cipherText.length % 16 != 0) {
			throw new RuntimeException("密文的byte数组不能为空且长度不能为0且长度必须为16的倍数。");
		}
		return sm4Utils.SG_DecECBData(keyBytes, cipherText);
	}
	/**
	 * 加密：string类型
	 * @param hexKey
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String hexKey, String plainText) {
		byte[] keyBytes = Util.hexToByte(hexKey);
		byte[] plainTextBytes = null;
		try {
			plainTextBytes = URLEncoder.encode(plainText, "UTF-8").getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("将明文字符串转换成byte数组失败", e);
		}
		return Util.byteToHex(encrypt(keyBytes, plainTextBytes));
	}
	/**
	 * 解密：string类型
	 * @param hexKey
	 * @param cipherText
	 * @return
	 */
	/*public static String decrypt(String hexKey, String cipherText) {
		byte[] keyBytes = Util.hexToByte(hexKey);
		byte[] cipherTextBytes = Util.hexToByte(cipherText);
		byte[] plainTextBytes = decrypt(keyBytes, cipherTextBytes);
		String plainText = null;
		try {
			plainText = URLDecoder.decode(new String(plainTextBytes, "UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("将解密后明文byte数组转换成字符串失败", e);
		}
		return plainText;
	}*/

}
