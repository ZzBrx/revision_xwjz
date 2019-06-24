package com.caiger.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.caiger.common.global.Global;

/**
 * @className: CookieUtils
 * @description: Cookie操作工具类
 * @author: 黄凯杰
 * @date: 2018年12月27日 上午10:00:00
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Component
public class CookieUtils {

	// 设置Cookie的域名 默认空，即当前访问的域名
	@Value("${shiro.cookie.domain}")
	private String domain;

	// 设置cookie的有效访问路径
	@Value("${shiro.cookie.path}")
	private String path;

	// 设置HttpOnly不能通过客户端脚本访问属性
	@Value("${shiro.cookie.httpOnly}")
	private boolean httpOnly;

	// 设置Cookie的过期时间，单位"天"
	@Value("${shiro.cookie.maxAge}")
	private int maxAge;

	/**
	 * @methodName: setCookie
	 * @description: 设置Cookie主方法
	 * @param response 响应对象
	 * @param name     名称
	 * @param value    值
	 * @param path     路径
	 * @param maxAge   生存时间
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 上午10:03:51
	 */
	public  void setCookie(HttpServletResponse response, String name, String value) {

		Cookie cookie = new Cookie(name, null);
		cookie.setPath(path);
		cookie.setDomain(domain);
		cookie.setHttpOnly(httpOnly);
		cookie.setMaxAge(maxAge * 60 * 60 * 24);
		try {
			cookie.setValue(URLEncoder.encode(value, Global.DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.addCookie(cookie);
	}

	/**
	 * @methodName: getCookie
	 * @description: 获得指定Cookie的值
	 * @param request  请求对象
	 * @param response 响应对象
	 * @param name     名字
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 上午10:55:05
	 */
	public String getCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					try {
						value = URLDecoder.decode(cookie.getValue(), Global.DEFAULT_ENCODING);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return value;
	}

	/**
	 * @methodName: removeCookie
	 * @description: 删除指定Cookie
	 * @param request  请求对象
	 * @param response 响应对象
	 * @param name     名字
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 上午10:56:19
	 */
	public void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	/**
	 * @methodName: removeAllCookie
	 * @description: 删除所有Cookie
	 * @param request  请求对象
	 * @param response 响应对象
	 * @param name     名字
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 上午10:58:47
	 */
	public void removeAllCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}

}
