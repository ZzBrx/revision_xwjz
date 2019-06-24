package com.caiger.module.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.caiger.common.global.Global;
import com.caiger.common.lang.StringUtils;
import com.caiger.common.utils.CookieUtils;
import com.caiger.common.utils.ServletUtils;
import com.caiger.module.sys.entity.Principal;
import com.caiger.module.sys.shiro.session.SessionDAO;

import lombok.extern.slf4j.Slf4j;


/**
 * @className:  LoginController   
 * @description: 登录控制器   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:25:21
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Controller
@RequestMapping("/")
@Slf4j
public class LoginController {

	//首页
	@Value("${shiro.successUrl}")
	private String successUrl;
	
	//登录页面
	@Value("${shiro.loginUrl}")
	private String loginUrl;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private CookieUtils cookieUtils;
	
	
	/**
	 * @methodName: login   
	 * @description: 进入登录页面   
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:25:57
	 */
	@GetMapping("login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model){
		
		//登录者对象
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		
		log.info("login active session size:{}", sessionDAO.getActiveSessions(false).size());
		
		cookieUtils.setCookie(response, "LOGINED", "false");
		
		//如果已经登录则跳转到首页
		if (principal!=null) {
			ServletUtils.redirectUrl(request, response, successUrl);
		}
		
		return "modules/sys/login";
	}
	
	/**
	 * @methodName: loginFailure   
	 * @description:登录失败，返回登录页面，真正的login的POST请求被过滤器处理了   
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:26:18
	 */
	@PostMapping("login")
	public String loginFailure(HttpServletRequest request, HttpServletResponse response, Model model) {
		//登录者对象
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		//跳转到首页
		if (principal!=null) {
			ServletUtils.redirectUrl(request, response, successUrl);
		}
		return "modules/sys/login";
	}
	

	/**
	 * @methodName: index   
	 * @description: shiro中已经登录成功，请求进入首页   
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午10:26:44
	 */
	@RequestMapping(value="index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model){
		
		//判断地址中是否包含JSESSIONID,如果包含去掉JSESSIONID
		if (StringUtils.containsIgnoreCase(request.getRequestURI(), ";JSESSIONID=")) {
			//重新请求首页
			ServletUtils.redirectUrl(request, response, successUrl);
			return null;
		}
		
		//登录者对象
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		//没有登录者对象，未加载shiro模块时没有登录者对象
		if (principal == null) {
			//退出登录
			SecurityUtils.getSubject().logout();
			//重定向到登录页面
			ServletUtils.redirectUrl(request, response, loginUrl);
			return null;
		}
		
		//如果是AJAX操作，返回json字符串
		if (ServletUtils.isAjaxRequest(request)) {
			model.addAttribute("result",Global.TRUE);
			model.addAttribute("message","登录成功");
			String render = ServletUtils.renderObject(response, model);
			return render;
		}
		
		//登录者信息
		model.addAttribute("principal", principal);
		
		return "modules/sys/index";
	}
	

}
