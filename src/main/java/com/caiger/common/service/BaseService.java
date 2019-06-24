package com.caiger.common.service;


import org.springframework.transaction.annotation.Transactional;

import com.caiger.module.sys.entity.User;

/**
 * @className:  BaseService   
 * @description: service抽象类  
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:57:11
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Transactional(readOnly = true)
public abstract class BaseService {
	
	/**
	 * @methodName: dataScopeFilter   
	 * @description: 数据过滤   
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月27日 下午2:48:05
	 */
	public static String dataScopeFilter(User user) {
		return null;
	}
	

}
