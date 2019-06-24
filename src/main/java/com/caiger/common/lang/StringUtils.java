package com.caiger.common.lang;


/**
 * @className:  StringUtils   
 * @description: 字符串处理工具   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:25:03
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	
	
	/**
	 * @methodName: inStringIgnoreCase   
	 * @description: 是否包含字符串   
	 * @param str 待验证字符串
	 * @param strings 字符串数组
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:36:50
	 */
	public static boolean inStringIgnoreCase(String str,String...strings) {
		
		if (str != null && strings != null){
        	for (String s : strings){
        		if (str.equalsIgnoreCase(trim(s))){
        			return true;
        		}
        	}
    	}
    	return false;
	}
	
	
	
	
	
	
	
}
