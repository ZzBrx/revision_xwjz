package com.caiger.common.utils;

import java.util.UUID;

import org.springframework.util.IdGenerator;

/**
 * @className:  IdGenUtils   
 * @description: 唯一编号生成器  
 * @author: 黄凯杰 
 * @date: 2019年1月15日 下午3:00:34
 * @version: V1.0.0
 * @copyright: Copyright © 2019 Caiger Digital Technology co., Ltd.
 */
public class IdGenUtils implements IdGenerator {
	
	/**
	 * @methodName: getNextId   
	 * @description: 获取新的18位长度的唯一编号 
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月15日 下午3:04:14
	 */
	public static String getId() {
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
		return Long.toString(idWorker.nextId());
	}
	
	/**
	 * @methodName: uuid   
	 * @description: 生成UUID中间无"-"  
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月15日 下午3:02:39
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * @methodName: generateId   
	 * @description: UUID    
	 * @return   
	 * @see org.springframework.util.IdGenerator#generateId()
	 */
	@Override
	public UUID generateId() {
		// TODO Auto-generated method stub
		return UUID.randomUUID();
	}
	
	
	

}
