package com.caiger.common.codec;

import java.io.UnsupportedEncodingException;
import com.caiger.common.global.Global;
import com.caiger.common.lang.StringUtils;


/**
 * @className:  Md5Utils   
 * @description: MD5不可逆算法  
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:39:40
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class Md5Utils {

	private static final String MD5 = "MD5";

	/**
	 * @methodName: md5   
	 * @description: MD5散列
	 * @param plaintext 明文
	 * @return String
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:39:52
	 */
	public static String md5(String plaintext) {
		return md5(plaintext, 1);
	}

	/**
	 * @methodName: md5   
	 * @description: MD5散列   
	 * @param plaintext 明文
	 * @param iterations 迭代次数
	 * @return String
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:41:06
	 */
	public static String md5(String plaintext, int iterations) {
		try {
			return EncodeUtils.encodeHex(md5(plaintext.getBytes(Global.DEFAULT_ENCODING),iterations));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;

	}
	
	/**
	 * @methodName: md5   
	 * @description: MD5散列   
	 * @param plaintext 明文
	 * @param salt 盐
	 * @param iterations 迭代次数
	 * @return String
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:41:51
	 */
	public static String md5(String plaintext, String salt, int iterations) {
		try {
			return EncodeUtils.encodeHex(md5(plaintext.getBytes(Global.DEFAULT_ENCODING), salt.getBytes(Global.DEFAULT_ENCODING), iterations));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	
	/**
	 * @methodName: md5   
	 * @description: MD5散列   
	 * @param plaintext 明文
	 * @return byte[]
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:42:20
	 */
	public static byte[] md5(byte[] plaintext) {
		return md5(plaintext, 1);
	}
	
	
	/**
	 * @methodName: md5   
	 * @description: MD5散列   
	 * @param plaintext  明文
	 * @param iterations 迭代次数
	 * @return byte[]
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:43:15
	 */
	public static byte[] md5(byte[] plaintext, int iterations) {
		return DigestUtils.digest(plaintext, MD5, null, iterations);
	}
	
	
	/**
	 * @methodName: md5   
	 * @description: MD5散列    
	 * @param input 明文
	 * @param salt 盐
	 * @param iterations 迭代次数
	 * @return byte[]
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:44:22
	 */
	public static byte[] md5(byte[] plaintext, byte[] salt, int iterations) {
		return DigestUtils.digest(plaintext, MD5, salt, iterations);
	}
	

}
