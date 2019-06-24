package com.caiger.module.sys.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.caiger.module.sys.entity.Principal;
import com.caiger.module.sys.entity.User;
import com.caiger.module.sys.service.UserService;



/**
 * @className:  AuthorizingRealm   
 * @description: 在认证、授权内部实现机制中都有提到，最终处理都将交给Realm进行处理   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:20:19
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class AuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm {

	
	@Autowired
	private UserService userService;
	
	/**
	 * @methodName: doGetAuthenticationInfo   
	 * @description: Shiro的认证过程最终会交由Realm执行，这时会调用Realm的getAuthenticationInfo(token)方法
	 * 	该方法主要执行以下操作:
	 *	 1、检查提交的进行认证的令牌信息;
	 *   2、根据令牌信息从数据源(通常为数据库)中获取用户信息;
	 *   3、对用户信息进行匹配验证;
	 *   4、验证通过将返回一个封装了用户信息的AuthenticationInfo实例;
	 *   5、验证失败则抛出AuthenticationException异常信息。    
	 * @param token
	 * @return
	 * @throws AuthenticationException   
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
		//查询数据库中用户信息
		User user = userService.getByUsername(authcToken.getUsername());
		if (user == null) {
			throw new AuthenticationException("没有此用户,请重新输入!");
		}
		
		return new SimpleAuthenticationInfo(new Principal(user),user.getPassword(), ByteSource.Util.bytes(user.getSalt()),getName());
	}

	
	/**
	 * @methodName: doGetAuthorizationInfo   
	 * @description: 权限授权是通过继承AuthorizingRealm抽象类，重载doGetAuthorizationInfo()
	 *               当访问到页面的时候，链接配置了相应的权限或者shiro标签才会执行此方法否则不会执行，
	 *               所以如果只是简单的身份认证没有权限的控制的话，那么这个方法可以不进行实现，直接返回null即可。
	 *               在这个方法中主要是使用类：SimpleAuthorizationInfo进行角色的添加和权限的添加。   
	 * @param principals
	 * @return   
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 当前登录用户
		Principal principal = (Principal) getAvailablePrincipal(principals);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		if (principal.getUsername().equals("admin")) {
			info.addStringPermission("sys:dse:update");
		}
		return info;
	}

	
	
}
