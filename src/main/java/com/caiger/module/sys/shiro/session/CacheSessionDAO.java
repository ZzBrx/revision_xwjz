package com.caiger.module.sys.shiro.session;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import com.caiger.common.lang.DateUtils;
import com.caiger.common.lang.StringUtils;

/**
 * @className:  CacheSessionDAO   
 * @description: 自定义CacheSessionDAO
 * @author: 黄凯杰 
 * @date: 2018年12月27日 上午11:44:26
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public class CacheSessionDAO extends EnterpriseCacheSessionDAO implements SessionDAO {

	/**
	 * @methodName: getActiveSessions   
	 * @description: 获取所有会话对象
	 * @param includeLeave 是否包含离线，true:包含，false：不包含
	 * @return   
	 * @see com.caiger.module.sys.shiro.session.SessionDAO#getActiveSessions(boolean)
	 */
	@Override
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return getActiveSessions(includeLeave,null,null);
	}

	/**
	 * @methodName: getActiveSessions
	 * @description: 当用户登录成功后，shiro会把用户名放到session的attribute中，
	 *               DefaultSubjectContext.PRINCIPALS_SESSION_KEY，这个属性记录的是用户名；
	 *               DefaultSubjectContext.AUTHENTICATED_SESSION_KEY，属性记录的是用户认证，用户登录成功后，这个attribute的值是true。
	 * @param includeLeave 是否包含离线，true:包含，false：不包含
	 * @param principal 当前登录者对象
	 * @param filterSession 需要过滤的session
	 * @return
	 * @see com.caiger.module.sys.shiro.session.SessionDAO#getActiveSessions(boolean,
	 *      java.lang.Object, org.apache.shiro.session.Session)
	 */
	@Override
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {

		// 包括离线，并且没有当前登录者对象
		if (includeLeave || principal == null) {
			return getActiveSessions();
		}

		Set<Session> sessions = new HashSet<>();
		// 不包括离线，并且有当前登录者对象
		if (!includeLeave && principal != null) {
			for (Session session : getActiveSessions()) {

				boolean isActiveSession = false;

				PrincipalCollection pc = (PrincipalCollection) session
						.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				// 如果为当前登录者对象的session则存下来
				if (principal.toString().equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)) {
					isActiveSession = true;
				}

				// 如果当前session访问时间小于3分钟则存下来
				if (DateUtils.pastMinutes(session.getLastAccessTime()) <= 3) {
					isActiveSession = true;
				}

				// 如果不为过滤的session则保存下来
				if (filterSession != null && !filterSession.getId().equals(session.getId())) {
					isActiveSession = true;
				}

				//保存session
				if (isActiveSession) {
					sessions.add(session);
				}

			}

		}

		return sessions;
	}

}
