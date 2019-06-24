package com.caiger.module.sys.shiro.session;

import java.util.Collection;

import org.apache.shiro.session.Session;

/**
 * @className:  SessionDAO   
 * @description: 自定义sessionDao接口   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:22:55
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public interface SessionDAO extends org.apache.shiro.session.mgt.eis.SessionDAO{
	
	/**
	 * @methodName: getActiveSessions   
	 * @description: 获取活动会话   
	 * @param includeLeave 是否包含离线
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:43:44
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave);
	
	
	/**
	 * @methodName: getActiveSessions   
	 * @description: 根据登录者对象获取活动会话   
	 * @param includeLeave 是否包含离线
	 * @param principal 登录者对象
	 * @param filterSession 过滤掉（不包含）这个session
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:43:57
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession);

}
