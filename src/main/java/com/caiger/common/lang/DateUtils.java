package com.caiger.common.lang;

import java.util.Date;

/**
 * @className:  DateUtils   
 * @description: 时间日期工具   
 * @author: 黄凯杰 
 * @date: 2018年12月27日 上午11:06:09
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	
	

	/**
	 * @methodName: pastMinutes   
	 * @description: 从输入时间到当前时间过去多少分钟
	 * @param date 
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 上午11:08:36
	 */
	public static long pastMinutes(Date date) {
		long time = new Date().getTime()-date.getTime();
		return time/(60*1000);
	}
	
	
	/**
	 * @methodName: pastHour   
	 * @description: 从输入时间到当前时间过去多少小时   
	 * @param date
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 上午11:09:16
	 */
	public static long pastHours(Date date) {
		long time = new Date().getTime()-date.getTime();
		return time/(60*60*1000);
	}
	
	
	
	/**
	 * @methodName: pastDays   
	 * @description: 从输入时间到当前时间过去多少天  
	 * @param date
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 上午11:10:03
	 */
	public static long pastDays(Date date) {
		long time = new Date().getTime()-date.getTime();
		return time/(24*60*60*1000);
	}
	
	
}
