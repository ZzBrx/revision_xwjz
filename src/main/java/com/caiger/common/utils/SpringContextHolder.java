package com.caiger.common.utils;

import org.apache.commons.lang3.Validate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @className:  SpringContextHolder   
 * @description: 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * @author: 黄凯杰 
 * @date: 2019年1月8日 下午1:31:44
 * @version: V1.0.0
 * @copyright: Copyright © 2019 Caiger Digital Technology co., Ltd.
 */
@Component
@Lazy(false)
@Slf4j
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
	
	private static ApplicationContext applicationContext = null;
	
	/**
	 * @methodName: getApplicationContext   
	 * @description: 取得存储在静态变量中的spring 上下文
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月8日 下午1:35:10
	 */
	public static ApplicationContext getApplicationContext() {
		validState();
		return applicationContext;
	}

	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        	SpringContextHolder.applicationContext = applicationContext;
		
	}
	
	
	/**
	 * @methodName: getBean   
	 * @description: 从  applicationContext中取出bean,转型成所赋值对象类型
	 * @param name
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月8日 下午1:38:33
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		validState();		
		return (T) applicationContext.getBean(name);
		
	}
	
	
	/**
	 * @methodName: getBean   
	 * @description: 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 * @param requiredType
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月8日 下午1:42:34
	 */
	public static <T> T getBean(Class<T> requiredType) {
		validState();
		return applicationContext.getBean(requiredType);
	}

	
	@Override
	public void destroy() throws Exception {
		
		if (log.isDebugEnabled()) {
			log.debug("清除applicationContext上下文..."+applicationContext);
		}
		applicationContext = null;
	}
	
	/**
	 * @methodName: validState   
	 * @description: 检查ApplicationContext不为空.  
	 * @author: 黄凯杰
	 * @date: 2019年1月8日 下午1:41:34
	 */
	private static void validState() {
		Validate.validState(applicationContext != null, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}

}
