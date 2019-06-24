package com.caiger.module.sys.utils;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import com.caiger.module.sys.entity.Principal;
import com.caiger.module.sys.entity.User;


/**
 * @className:  PrincipalUtils   
 * @description: 认证对象工具类  
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:24:10
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Component("principalUtils")
public class UserUtils {
	
	//缓存用户信息
	public static final String CACHE_USER_LOGIN_NAME_ = "cache_user_login_name_";
	
	public static final String CACHE_USER_ID_ = "cache_user_id_";
	
	/*//缓存菜单信息
	public static final String CACHE_MENU_ = "cache_menu_";
	
	//缓存角色列表
	public static final String CACHE_ROLE_LIST_ = "cache_role_list_";
	
	//缓存菜单列表
	public static final String CACHE_MENU_LIST_ = "cache_menu_list_";
	
	//缓存组织机构列表
	public static final String CACHE_ORG_LIST_ = "cache_org_list_";
	
	//缓存所有组织机构
	public static final String CACHE_ORG_ALL_LIST = "cache_org_all_list";*/
	
	//用户服务
	//private static UserService userService = SpringContextHolder.getBean(UserService.class);
			

	
	/**
	 * @methodName: getSubject   
	 * @description: 获取授权主要对象   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:42:54
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	
	/**
	 * @methodName: getPrincipal   
	 * @description: 获取认证登录者对象  
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:43:04
	 */
	public static Principal getPrincipal() {
		Subject subject = SecurityUtils.getSubject();
		Principal principal = (Principal)subject.getPrincipal();
		return principal;
	}
	
	/**
	 * @methodName: getActiveUser   
	 * @description: 当前登录者对象  
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月7日 下午5:18:41
	 */
	public static User getActiveUser() {
		return getPrincipal().getActiveUser();
	}
	
	
	
	

}
