package com.caiger.common.utils;

import java.util.Iterator;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

/**
 * @className:  CacheUtils   
 * @description: Cache缓存操作组件   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:57:49
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class CacheUtils {
	
	private static CacheManager cacheManager =  SpringContextHolder.getBean(CacheManager.class);;
	
	public static final String SYS_CACHE = "sysCache";
	
	/**
	 * @methodName: get   
	 * @description: 根据Key获取系统缓存中value  
	 * @param key
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:58:16
	 */
	public static Object get(String key) {
		return getCache(SYS_CACHE).get(key);
	}
	
	/**
	 * @methodName: put   
	 * @description: 设置缓存   
	 * @param key
	 * @param value
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:58:31
	 */
	public static void put(String key, Object value) {
		getCache(SYS_CACHE).put( key, value);
	}
	
	/**
	 * @methodName: remove   
	 * @description: 根据key清除缓存   
	 * @param key
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:58:43
	 *//*
	public static void remove(String key) {
		getCache(SYS_CACHE).remove(key);
	}
	
	/**
	 * @methodName: removeAll   
	 * @description: 清除所有缓存   
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:58:53
	 *//*
	public static void removeAll() {
		Set<String> keys = getCache(SYS_CACHE).keys();
		for (Iterator<String> it = keys.iterator(); it.hasNext();){
			getCache(SYS_CACHE).remove(it.next());
		}
	}
	
	
	/**
	 * @methodName: removeAll   
	 * @description: 清除所有缓存   
	 * @param cacheName 指定缓存名
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:58:53
	 */
	public static void removeAll(String cacheName) {
		Set<String> keys = getCache(cacheName).keys();
		for (Iterator<String> it = keys.iterator(); it.hasNext();){
			getCache(cacheName).remove(it.next());
		}
	}
	
	
	/**
	 * 获得一个Cache，没有则显示日志。
	 * @param cacheName 指定缓存名
	 * @return
	 */
	private static Cache<String, Object> getCache(String cacheName){
		Cache<String, Object> cache = cacheManager.getCache(cacheName);
		if (cache == null){
			throw new RuntimeException("当前系统中没有定义“"+cacheName+"”这个缓存。");
		}
		return cache;
	}

	
}
