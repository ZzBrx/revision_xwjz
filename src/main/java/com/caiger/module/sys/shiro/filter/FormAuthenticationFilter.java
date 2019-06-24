package com.caiger.module.sys.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import com.caiger.common.lang.StringUtils;
import com.caiger.common.utils.ServletUtils;
import com.caiger.module.sys.shiro.realm.UsernamePasswordToken;

import lombok.extern.slf4j.Slf4j;

/**
 * @className:  FormAuthenticationFilter   
 * @description: 表单验证（包含验证码）过滤类，authc所有地址都将经过此过滤  
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:18:16
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Slf4j
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	
	public static final String DEFAULT_MESSAGE_PARAM = "message";
	/**
	 * @methodName: onAccessDenied   
	 * @description: TODO(这里用一句话描述这个方法的作用)    
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 * @throws Exception   
	 * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

		// 判断是否为登录请求，比较登录地址是否与loginUrl一致
		if (this.isLoginRequest(request, response)) {
			//判断是或否为POST请求，如果是POST请求则为true
			if (this.isLoginSubmission(request, response)) {
				//执行登录操作
				return this.executeLogin(request, response);
			} else {
				return true;
			}
		} else {
			//将当前请求保存起来并重定向到登录页面
			this.saveRequestAndRedirectToLogin(request, response);
			return false;
		}

	}


	/**
	 * @methodName: createToken   
	 * @description:  创建登录授权令牌   
	 * @param request
	 * @param response
	 * @return   
	 * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#createToken(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		log.info("createToken: username:{};password:{};rememberMe：{};host:{};",username,password,rememberMe,host);
		return new UsernamePasswordToken(username, password, rememberMe, host);

	}
	
	

	/**
	 * @methodName: onLoginFailure   
	 * @description: 登录失败，将登录异常信息放入request中    
	 * @param token
	 * @param e
	 * @param request
	 * @param response
	 * @return   
	 * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginFailure(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationException, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		//异常名
		String className = e.getClass().getName();
		//显示错误信息
		String message = StringUtils.EMPTY;
		if (IncorrectCredentialsException.class.getName().equals(className)
				|| UnknownAccountException.class.getName().equals(className)) {
			message = "输入用户名或者密码错！";
		}else if (e.getMessage()!=null) {
			message = e.getMessage();
		}else {
			message = "系统出现点问题，请稍后再试！";
		}
		 request.setAttribute(getFailureKeyAttribute(), className);
		 request.setAttribute("message", message);
		
		return true;	
	}
	
	
	
	/**
	 * @methodName: onLoginSuccess   
	 * @description: 登录成功 
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception   
	 * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginSuccess(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.subject.Subject, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {

		//登录操作如果是AJAX请求，分发返回登录成功页面
		if (ServletUtils.isAjaxRequest((HttpServletRequest)request)) {
			//分发到successUrl页面
			request.getRequestDispatcher(getSuccessUrl()).forward(request, response);
		}
		else {
			//重定向
			WebUtils.issueRedirect(request, response, getSuccessUrl());
		}
		return false;
	}
	

}
