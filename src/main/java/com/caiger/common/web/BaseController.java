package com.caiger.common.web;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caiger.common.codec.Md5Utils;
import com.caiger.common.global.Global;
import com.caiger.common.lang.StringUtils;
import com.caiger.common.mapper.JsonMapper;
import com.caiger.common.utils.ServletUtils;
import com.github.pagehelper.PageHelper;

/**
 * @className:  BaseController   
 * @description: 控制器基类   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:59:12
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class BaseController {
	
	/**
	 * @methodName: startPage   
	 * @description: 初始化分页插件
	 * @param request
	 * @author: 黄凯杰
	 * @date: 2019年1月9日 下午4:33:40
	 */
	protected void startPage(HttpServletRequest request) {
		
		int pageNum = StringUtils.isBlank(request.getParameter("pageNum"))?1:Integer.parseInt(request.getParameter("pageNum"));
		int pageSize = StringUtils.isBlank(request.getParameter("pageSize"))?30:Integer.parseInt(request.getParameter("pageSize"));
		PageHelper.startPage(pageNum, pageSize);
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
	protected static String renderObject(HttpServletResponse response, Object object) {
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
	protected static String renderString(HttpServletResponse response, String string) {
		return ServletUtils.renderString(response, string, null);
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
	protected static String renderResult(String result, String message) {
		return ServletUtils.renderResult(result, message,null);
	}
	
	/**
	 * @methodName: renderResult   
	 * @description: 将结果渲染到客户端 
	 * @param result Global.TRUE or Globle.FALSE
	 * @param message 消息
	 * @param data 对象数据
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月9日 下午4:38:11
	 */
	protected String renderResult(String result, String message, Object data) {
		return ServletUtils.renderResult(result, message, data);
	}
	
	
	/**
	 * @methodName: text   
	 * @description: 使用占位符生成字符串 
	 * @param pattern 规则
	 * @param message 消息集合
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月10日 下午2:58:50
	 */
	protected String text(String pattern,Object... message) {
		return MessageFormat.format(pattern, message);
	}
	
	
	/**
	 * @methodName: cipherEncryption   
	 * @description: 密码加密  
	 * @param plaintext 明文
	 * @param algorithm 加密方式
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月12日 下午1:13:39
	 */
	protected String cipherEncryption(String plaintext,String algorithm) {
		return cipherEncryption(plaintext,null,algorithm);
	}
	
	
	/**
	 * @methodName: cipherEncryption   
	 * @description: 密码加密  
	 * @param plaintext 明文
	 * @param salt 盐
	 * @param algorithm 加密方式
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月12日 下午1:13:20
	 */
	protected String cipherEncryption(String plaintext, String salt, String algorithm) {
		return cipherEncryption(plaintext,salt,algorithm,1);
	}
	
	
	/**
	 * @methodName: CipherEncryption   
	 * @description: 密码加密     
	 * @param plaintext 明文
	 * @param salt 盐
	 * @param algorithm 加密方式
	 * @param iterations 迭代次数
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月12日 下午1:10:12
	 */
	protected String cipherEncryption(String plaintext, String salt, String algorithm, int iterations) {
		
		String ciphertext = "";
		if (algorithm.equalsIgnoreCase(Global.MD5)) {
			
			if (StringUtils.isBlank(salt)) {
				ciphertext = Md5Utils.md5(plaintext, iterations);
			}else {
				ciphertext = Md5Utils.md5(plaintext, salt, iterations);
			}
		}
		return ciphertext;
	}

}
