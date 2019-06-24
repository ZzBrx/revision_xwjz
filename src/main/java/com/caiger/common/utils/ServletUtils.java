package com.caiger.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.caiger.common.global.Global;
import com.caiger.common.lang.StringUtils;
import com.caiger.common.mapper.JsonMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * @className:  ServletUtils   
 * @description: servlet工具类   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:54:03
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Slf4j
public class ServletUtils {
	

	/**
	 * @methodName: isAjaxRequest   
	 * @description: 是否是ajax请求   
	 * @param request
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:55:42
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		
		String accept = request.getHeader("accept");
		if (accept != null && accept.indexOf("application/json") != -1){
			return true;
		}

		String xRequestedWith = request.getHeader("X-Requested-With");
		if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1){
			return true;
		}
		
		String uri = request.getRequestURI();
		if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml")){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * @methodName: redirectUrl   
	 * @description: 重定向，支持ajax  
	 * @param request
	 * @param response
	 * @param url
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:56:10
	 */
	public static void redirectUrl(HttpServletRequest request, HttpServletResponse response, String url){
		try {
			if (ServletUtils.isAjaxRequest(request)){
				request.getRequestDispatcher(url).forward(request, response); // AJAX不支持Redirect改用Forward
			}else{
				response.sendRedirect(request.getContextPath() + url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @methodName: getRemoteAddr   
	 * @description: 获得远程地址   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:56:33
	 */
	public static String getRemoteAddr(){
		String remoteAddr = getRequest().getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = getRequest().getHeader("X-Forwarded-For");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr =  getRequest().getHeader("Proxy-Client-IP");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr =  getRequest().getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr :  getRequest().getRemoteAddr();
	}
	
	
	/**
	 * @methodName: getRequest   
	 * @description: 获取当前请求对象   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:56:50
	 */
	public static HttpServletRequest getRequest(){
		try{
			return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * @methodName: renderObject   
	 * @description: 将对象渲染到客户端
	 * @param response
	 * @param object
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午3:41:59
	 */
	public static String renderObject(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJson(object));
	}
	
	
	/**
	 * @methodName: renderString   
	 * @description: 将字符串渲染到客户端
	 * @param response 渲染对象
	 * @param string 待渲染的字符串
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午5:21:54
	 */
	public static String renderString(HttpServletResponse response, String string) {
		return renderString(response, string, null);
	}
	
	
	
	/**
	 * @methodName: renderString   
	 * @description: 将字符串渲染到客户端页面
	 * @param response 渲染对象
	 * @param string 待渲染字符串
	 * @param type 指定返回类型
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午5:15:31
	 */
	public static String renderString(HttpServletResponse response, String string, String type) {
		
		
		try {
			//未指定类型时,根据传入字符串判断需要返回的类型
			if (type == null){
				if ((StringUtils.startsWith(string, "{") && StringUtils.endsWith(string, "}")) || (StringUtils.startsWith(string, "[") && StringUtils.endsWith(string, "]"))){
					type = "application/json";
				}else if (StringUtils.startsWith(string, "<") && StringUtils.endsWith(string, ">")){
					type = "application/xml";
				}else{
					type = "text/html";
				}
			}
			response.setContentType(type);
			response.setCharacterEncoding(Global.DEFAULT_ENCODING);
			response.getWriter().print(string);
		} catch (IOException e) {
			log.info("ServletUtils renderString error: string:{};type:{} ",string,type);
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	/**
	 * @methodName: renderResult   
	 * @description: 返回结果类型字符串 
	 * @param result Global.TRUE or Globle.FALSE
	 * @param message 返回的消息
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月9日 下午4:44:47
	 */
	public static String renderResult(String result, String message) {
		return renderResult(result, message,null);
	}

	
	/**
	 * @methodName: renderResult
	 * @description: 返回结果类型字符串      
	 * @param result Global.TRUE or Globle.FALSE
	 * @param message 返回的消息
	 * @param data 消息数据
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月9日 下午4:42:39
	 */
	@SuppressWarnings("unchecked")
	public static String renderResult(String result, String message, Object data) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result);
		resultMap.put("message", message);
		if (data != null){
			if (data instanceof Map){
				resultMap.putAll((Map<String, Object>)data);
			}else{
				resultMap.put("data", data);
			}
		}
		return JsonMapper.toJson(resultMap);
		
	}
	
	
	
	
	
}
