package com.caiger.module.sys.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @className:  Principal   
 * @description: 当前授权用户信息   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:41:49
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Setter
@Getter
public class Principal implements Serializable{
	
	/**@Fields serialVersionUID : TODO
	 */ 
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String username;
	
	private String name;
	
	private User activeUser;
	
	/**
	 * @methodName: Principal   
	 * @description: 构造函数  
	 * @param user   用户
	 * @throws
	 * @author: 黄凯杰 
	 * @date: 2018年12月26日 下午10:42:04
	 */
	public Principal(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.name = user.getName();
		this.activeUser = user;
	}

}
