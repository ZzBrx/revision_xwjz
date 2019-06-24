package com.caiger.common.codec;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * @className:  DigestUtils   
 * @description: 不可逆算法   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:09:35
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class DigestUtils {
	
	private static SecureRandom random = new SecureRandom();

	/**
	 * @methodName: digest   
	 * @description: 对字符串进行散列   
	 * @param inputText 需要散列的字符串
	 * @param algorithm 散列算法（"SHA-1"、"MD5"）
	 * @param salt 盐
	 * @param iterations 迭代次数
	 * @return byte[]
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:12:11
	 */
	public static byte[] digest(byte[] inputText, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}
			byte[] result = digest.digest(inputText);
			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * @methodName: digest   
	 * @description: 对文件进行散列  
	 * @param input  需要散列的流
	 * @param algorithm 散列算法（"SHA-1"、"MD5"）
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:14:31
	 */
	public static byte[] digest(InputStream input, String algorithm) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(algorithm);

			int bufferLength = 1024 * 8;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);
			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * @methodName: byteToHex   
	 * @description: byte => hex   
	 * @param data
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月12日 下午1:52:14
	 */
	public static String byteToHex(byte[] data) {
 		StringBuffer sb = new StringBuffer();

 		for (int i = 0; i < data.length; i++) {
 			String hexStr = Integer.toHexString(data[i] & 0xFF);
 			if (hexStr.length() == 1) {
 				hexStr = "0" + hexStr;
 				sb.append(hexStr);
 			} else {
 				sb.append(hexStr.toUpperCase());
 			}
 		}
 		
 		return sb.toString();
 	}
	
	/**
	 * 生成随机的Byte[]作为salt.
	 * 
	 * @param numBytes byte数组的大小
	 */
	public static byte[] generateSalt(int numBytes) {
		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}
	
}
