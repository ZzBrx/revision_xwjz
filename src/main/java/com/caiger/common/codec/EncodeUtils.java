package com.caiger.common.codec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


/**
 * @className:  EncodeUtils   
 * @description: 编码解码工具   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:19:32
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class EncodeUtils {

	/**
	 * @methodName: encodeHex   
	 * @description: byte[] To Hex  
	 * @param byteArray 字节数组
	 * @return String
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:19:52
	 */
	public static String encodeHex(byte[] byteArray) {
		return new String(Hex.encodeHex(byteArray));
	}

	/**
	 * @methodName: decodeHex   
	 * @description:  Hex to byte[] 
	 * @param str Hex to byte[]
	 * @return byte[]
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:22:43
	 */
	public static byte[] decodeHex(String str) {
		try {
			return Hex.decodeHex(str.toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
