package com.caiger.module.sys.config;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.caiger.module.sys.shiro.filter.FormAuthenticationFilter;
import com.caiger.module.sys.shiro.realm.AuthorizingRealm;
import com.caiger.module.sys.shiro.session.CacheSessionDAO;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import net.sf.ehcache.CacheManager;


/**
 * @className:  ShiroConfig   
 * @description: Shiro 配置  
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:59:43
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Configuration  // @Configuration用于定义配置类，可替换xml配置文件
public class ShiroConfig {

	// 没有登录的用户请求需要登录的页面时，自动跳转到登录页面
	@Value("${shiro.loginUrl}")
	private String loginUrl;

	// 权限认证失败地址
	@Value("${shiro.unauthorizedUrl}")
	private String unauthorizedUrl;

	// Shiro配置
	@Value("${shiro.filterChainDefinitions}")
	private String filterChainDefinitions;

	// 登录成功后地址
	@Value("${shiro.successUrl}")
	private String successUrl;

	// session超时时间，单位为毫秒，默认填写分钟
	@Value("${shiro.session.expireTime}")
	private int expireTime;

	// 默认ehcache名称
	@Value("${shiro.ehcacheName}")
	private String ehcacheName;
	
	// ehcache-shiro配置文件路径
	@Value("${shiro.ehcacheLocation}")
	private String ehcacheLocation;

	// cookie有效访问路径
	@Value("${shiro.cookie.path}")
	private String path;

	// cookie有效时长，单位为毫秒，默认填写天
	@Value("${shiro.cookie.maxAge}")
	private int maxAge;

	// 设置HttpOnly不能通过客户端脚本访问属性
	@Value("${shiro.cookie.httpOnly}")
	private boolean httpOnly;

	// 设置Cookie的域名
	@Value("${shiro.cookie.domain}")
	private String domain;
	
	// shiro凭证匹配器加密方式
	@Value("${shiro.matcher.algorithm}")
	private String algorithm;
	
	// shiro证匹配器迭代次数
	@Value("${shiro.matcher.iterations}")
	private int iterations;
	
	/**
	 * @methodName: shiroFilter   
	 * @description: 处理拦截资源文件问题;在初始化ShiroFilterFactoryBean的时候需要注入SecurityManager; Filter Chain定义说明 : 1、一个URL可以配置多个Filter，使用逗号分隔;2、当设置多个过滤器时，全部验证通过，才视为通过; 3、部分过滤器可指定参数，如perms，roles.   
	 * @param securityManager 核心安全管理器
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:00:01
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {

		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		// 设置安全事务管理器
		bean.setSecurityManager(securityManager);
		
		// 设置登录页面URL
		bean.setLoginUrl(loginUrl);
		// 设置权限认证失败跳转地址
		bean.setUnauthorizedUrl(unauthorizedUrl);
		// 设置权限认证成功后跳转地址
		bean.setSuccessUrl(successUrl);
        
	    // Shiro连接约束配置，即过滤链的定义
	    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/plugins/**", "anon");//静态资源匿名访问
        filterChainDefinitionMap.put("/captcha", "anon");//验证码匿名访问
        filterChainDefinitionMap.put("/logout", "logout");// 退出 logout地址，shiro去清除session
        filterChainDefinitionMap.put("/**", "authc");// 需要拦截的访问
        
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap); 
        // 设置过滤器
        Map<String, Filter> filters = bean.getFilters();
		filters.put("authc", formAuthenticationFilter());
		bean.setFilters(filters);
		

		
		return bean;

	}
	

	/**
	 * @methodName: formAuthenticationFilter   
	 * @description: 配置登录过滤器   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:00:36
	 */
	public FormAuthenticationFilter formAuthenticationFilter() {
		FormAuthenticationFilter bean = new FormAuthenticationFilter();
		return bean;
	}
	


	/**
	 * @methodName: securityManager   
	 * @description: 配置核心安全事务管理器   
	 * @param authorizingRealm 自定义的权限登录器
	 * @param sessionManager 会话管理器
	 * @param cacheManager 缓存管理器
	 * @param rememberMeManager 记住我管理器
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:00:57
	 */
	@Bean(name = "securityManager")
	public SecurityManager securityManager(
			
			@Qualifier("authorizingRealm") AuthorizingRealm authorizingRealm,
			@Qualifier("sessionManager") SessionManager sessionManager,
			@Qualifier("cacheManager") EhCacheManager cacheManager,
			@Qualifier("rememberMeManager") CookieRememberMeManager rememberMeManager) {
		
		DefaultWebSecurityManager bean = new DefaultWebSecurityManager();
		// 将realm注入安全管理器
		bean.setRealm(authorizingRealm);
		// 将session会话管理器注入安全管理器
		bean.setSessionManager(sessionManager);
		// 将ehCache缓存注入安全管理器
		bean.setCacheManager(cacheManager);
		// 记住我cookie管理器注入安全管理器
		bean.setRememberMeManager(rememberMeManager);

		return bean;
	}


	/**
	 * @methodName: authorizingRealm   
	 * @description: 自定义的权限登录器   
	 * @param cacheManager 缓存管理器
	 * @param credentialsMatcher 凭证匹配器
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:02:34
	 */
	@Bean(name = "authorizingRealm")
	public AuthorizingRealm authorizingRealm(@Qualifier("cacheManager") EhCacheManager cacheManager, @Qualifier("credentialsMatcher") HashedCredentialsMatcher credentialsMatcher) {
		AuthorizingRealm bean = new AuthorizingRealm();
		bean.setCredentialsMatcher(credentialsMatcher);
		bean.setCacheManager(cacheManager);
		return bean;
	}

	/**
	 * @methodName: sessionManager   
	 * @description: 会话管理器  
	 * @param sessionDAO 会话 DAO
	 * @param cacheManager 缓存管理器
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:02:56
	 */
	@Bean(name = "sessionManager")
	public SessionManager sessionManager( @Qualifier("sessionDAO") SessionDAO sessionDAO, @Qualifier("cacheManager") EhCacheManager cacheManager) {
		DefaultWebSessionManager bean = new DefaultWebSessionManager();
		// 设置自定义sessionDAO
		bean.setSessionDAO(sessionDAO);
		// 设置缓存管理器
		bean.setCacheManager(cacheManager);
		// 设置删除过期session
		bean.setDeleteInvalidSessions(true);
		// 设置session超时时间
		bean.setGlobalSessionTimeout(expireTime * 60 * 1000);
		// 去掉JSESSIONID
		bean.setSessionIdUrlRewritingEnabled(false);
		// 定期检查session
		bean.setSessionValidationSchedulerEnabled(true);
		return bean;

	}

	/**
	 * @methodName: sessionDAO   
	 * @description: 自定义SessionDAO
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:03:39
	 */
	@Bean(name = "sessionDAO")
	public SessionDAO sessionDAO() {
		CacheSessionDAO bean = new CacheSessionDAO();
		return bean;
		
	}
	
	/**
	 * @methodName: cacheManager   
	 * @description: Ehcache缓存管理器   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:03:55
	 */
	@Bean(name = "cacheManager")
	public EhCacheManager cacheManager() {
		CacheManager cacheManager = CacheManager.getCacheManager(ehcacheName);
		EhCacheManager bean = new EhCacheManager();
		if (cacheManager == null) {
			bean.setCacheManagerConfigFile(ehcacheLocation);
		} else {
			bean.setCacheManager(cacheManager);
		}
		return bean;
	}


	/**
	 * @methodName: rememberMeManager   
	 * @description: cookie记住我管理器  
	 * @param cookie
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:04:19
	 */
	@Bean(name = "rememberMeManager")
	public CookieRememberMeManager rememberMeManager(@Qualifier("cookie") SimpleCookie cookie) {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(cookie);
		// cookieRememberMeManager.setCipherKey(Base64.decode("fCq+/xW488hMTCD+cmJ3aQ=="));
		return cookieRememberMeManager;
	}
	
	/**
	 * @methodName: cookie   
	 * @description: ookie设置  
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:04:56
	 */
	@Bean(name="cookie")
	public SimpleCookie cookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie bean = new SimpleCookie(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		// 记住我cookie生效时间30天 ,单位秒;
		bean.setMaxAge(maxAge * 24 * 60 * 60);
		// 不能通过客户端脚本访问属性
		bean.setHttpOnly(httpOnly);
		//设置Cookie有效访问路径
		bean.setPath(path);
		//设置Cookie域名
		bean.setDomain(domain);
		return bean;
	}
	
	/**
	 * @methodName: credentialsMatcher   
	 * @description: shiro 凭证匹配器   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:05:21
	 */
	@Bean(name="credentialsMatcher")
	public HashedCredentialsMatcher credentialsMatcher(){
		HashedCredentialsMatcher bean = new HashedCredentialsMatcher();
		bean.setHashAlgorithmName(algorithm);//散列算法:这里使用MD5算法;
		bean.setHashIterations(iterations);//散列的次数，比如散列两次，相当于 md5(md5(""));
		return bean;
	}


	/**
	 * @methodName: shiroDialect   
	 * @description: shiro与模板引擎匹配   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:05:35
	 */
	@Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
	

	/**
	 * @methodName: defaultAdvisorAutoProxyCreator   
	 * @description: 过滤器代理配置,开启shiro注解   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:05:59
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator bean = new DefaultAdvisorAutoProxyCreator();
		bean.setProxyTargetClass(true);
		return bean;
	}
	

	/**
	 * @methodName: authorizationAttributeSourceAdvisor   
	 * @description: 启用Shrio授权注解拦截方式，AOP式方法级权限检查   
	 * @param securityManager
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:06:12
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor( @Qualifier("securityManager") SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor bean = new AuthorizationAttributeSourceAdvisor();
		bean.setSecurityManager(securityManager);
		return bean;
	}
	

}
