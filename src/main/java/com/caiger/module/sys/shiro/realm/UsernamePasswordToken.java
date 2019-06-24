package com.caiger.module.sys.shiro.realm;

/**
 * @className:  UsernamePasswordToken   
 * @description: 自定义Token令牌  
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:22:16
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public UsernamePasswordToken() {
		
	}
	
	
	/**
	 * @Title:  FormToken   
	 * @Description:TODO 
	 * @param username 用户名
	 * @param password 密码
	 * @param rememberMe 记住我
	 * @param host 客户端地址
	 * @param captcha 验证码
	 * @throws
	 */
	public UsernamePasswordToken(String username, char[] password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}
	
	/**
	 * @Title:  FormToken   
	 * @Description:TODO 
	 * @param username 用户名
	 * @param password 密码
	 * @param rememberMe 记住我
	 * @param host 客户端地址
	 * @param captcha 验证码 
	 * @throws
	 */
	public UsernamePasswordToken(String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}
	

}
