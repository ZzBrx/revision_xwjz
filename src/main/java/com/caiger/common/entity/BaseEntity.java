package com.caiger.common.entity;

import java.io.Serializable;
import java.util.Map;

import com.caiger.module.sys.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @className:  BaseEntity   
 * @description: 基类   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:49:35
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Getter
@Setter
public abstract class BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String ROOT_ID = "0"; 
	
	//状态正常
	public static final String STATUS_NORMAL = "0";
		
	//状态停用
	public static final String STATUS_DISABLE = "1";

	/**
	 *  唯一编号
	 */
	protected String id;
	
	/**
	 * 当前登录用户
	 */
	public User activeUser;
	
	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;
	
	public BaseEntity() {
	
	}
	
	/**
	 * @methodName: BaseEntity   
	 * @description: 构造函数，应用反射可以创建一个新的对象 
	 * parentEntity = entityClass.getConstructor(String.class).newInstance("0");
	 * @param id  
	 * @throws
	 * @author: 黄凯杰 
	 * @date: 2019年2月26日 下午2:19:55
	 */
	public BaseEntity(String id) {
		this.id = id;
	}

	@JsonIgnore
	public Map<String, String> getSqlMap() {
		return sqlMap;
	}
	


	
}
