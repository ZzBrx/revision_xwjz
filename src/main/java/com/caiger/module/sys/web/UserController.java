package com.caiger.module.sys.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.caiger.common.codec.Md5Utils;
import com.caiger.common.global.Global;
import com.caiger.common.lang.StringUtils;
import com.caiger.common.web.BaseController;
import com.caiger.module.sys.entity.Acredit;
import com.caiger.module.sys.entity.Dse;
import com.caiger.module.sys.entity.User;
import com.caiger.module.sys.service.AcreditService;
import com.caiger.module.sys.service.DseService;
import com.caiger.module.sys.service.UserService;
import com.caiger.module.sys.utils.ToolUtil;
import com.github.pagehelper.PageInfo;

/**
 * @className: UserController
 * @description: 用户控制器
 * @author: 黄凯杰
 * @date: 2018年12月26日 下午10:39:16
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Controller
@RequestMapping("sys/user/")
public class UserController extends BaseController {

	// shiro凭证匹配器加密方式
	@Value("${shiro.matcher.algorithm}")
	private String algorithm;

	// shiro证匹配器迭代次数
	@Value("${shiro.matcher.iterations}")
	private int iterations;

	@Autowired
	private UserService userService;
	
	@Autowired
	private DseService dseService;

	@Autowired
	private AcreditService acreditService;
	
	@Autowired
	private Dse dse;

	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/user/userIndex";
	}

	@RequestMapping(value = "list")
	public String list(User user, Model model) {
		return "modules/sys/user/userList";
	}

	@RequestMapping(value = "listData")
	@ResponseBody
	public PageInfo<User> listData(User user, HttpServletRequest request, HttpServletResponse response) {
		startPage(request);
		List<User> userList = userService.getList(user);
		PageInfo<User> pageInfo = new PageInfo<User>(userList);
		return pageInfo;
	}

	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (StringUtils.isNotBlank(user.getId())) {
			user = userService.get(user);
		}
		model.addAttribute("user", user);
		return "modules/sys/user/userForm";
	}

	@RequestMapping(value = "save")
	@ResponseBody
	public String save(User user, HttpServletRequest request, Model model) throws UnsupportedEncodingException {

		if (StringUtils.isBlank(user.getId())) {
			String inputText = Global.DEFAULT_PASSWORD;
			String salt = inputText.substring(0, 6);
			user.setSalt(salt);
			user.setPassword(Md5Utils.md5(user.getPd(), salt, iterations));
		}
		userService.save(user);
		return renderResult(Global.TRUE, text("保存用户【{0}】成功", user.getUsername()));
	}

	/**
	 * @methodName: checkUsername
	 * @description: 检查用户名
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午1:08:06
	 */
	@RequestMapping(value = "checkUsername")
	@ResponseBody
	public String checkUsername(User user) {

		if (user.getUsername() != null && user.getUsername().equals(user.getOldUsername())) {
			return Global.TRUE;
		} else if (user.getUsername() != null && userService.getByUsername(user.getUsername()) == null) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}

	/**
	 * @methodName: checkCode
	 * @description: 检查用户编号
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午1:08:06
	 */
	@RequestMapping(value = "checkCode")
	@ResponseBody
	public String checkCode(User user) {

		if (user.getUserCode() != null && user.getUserCode().equals(user.getOldUserCode())) {
			return Global.TRUE;
		} else if (user.getUserCode() != null && userService.getByCode(user) == null) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(User user) {
		userService.delete(user);
		return renderResult(Global.TRUE, text("删除用户【" + user.getUsername() + "】成功"));
	}

	/**
	 * @methodName: enable
	 * @description: 重置密码
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月6日 上午8:56:20
	 */
	@RequestMapping(value = "resetpwd")
	@ResponseBody
	public String resetpwd(User user) {
		String salt = Global.DEFAULT_PASSWORD.substring(0, 6);
		user.setSalt(salt);
		user.setPassword(Md5Utils.md5(Global.DEFAULT_PASSWORD, salt, iterations));
		userService.updatePasswordById(user);
		return renderResult(Global.TRUE, text("重置用户密码【{0}】成功", user.getUsername()));
	}

	/**
	 * 分配设备
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "allocateDevList")
	public String allocateDevList(User user, Model model) {
		model.addAttribute("user", user);
		return "modules/sys/user/allocateDevList";
	}

	@RequestMapping(value = "allocateDevListData")
	@ResponseBody
	public PageInfo<Acredit> allocateDevListData(User user,Acredit acredit, HttpServletRequest request, HttpServletResponse response) {

		acredit.setUserid(user.getId());
		startPage(request);
		List<Acredit> acreditList = acreditService.getDevList(acredit);
		PageInfo<Acredit> pageInfo = new PageInfo<Acredit>(acreditList);
		return pageInfo;
	}

	// 批量删除
	@RequestMapping(value = "deleteBatches")
	@ResponseBody
	public String deleteBatches(Acredit acredit) {
		if (!acredit.getDevid().equals("")) {
			String[] split = acredit.getDevid().split(",");
			List<String> idList = Arrays.asList(split);
			acreditService.deleteBatches(idList);
			return renderResult(Global.TRUE, text("删除成功"));
		}
		return null;
	}

	// 单个删除
	@RequestMapping(value = "deleteAuthDev")
	@ResponseBody
	public String deleteAuthDev(Acredit acredit) {
		if (!acredit.getDevid().equals("")) {
			acreditService.delete(acredit);
			return renderResult(Global.TRUE, text("删除成功"));
		}
		return null;
	}

	// 分配新的设备
	@RequestMapping(value = "allocateNewDevList")
	public String allocateNewDevList(User user, Model model) {
		model.addAttribute("user", user);
		return "modules/dse/dse_mgt/devSelectList";
	}

	@RequestMapping(value = "unauthorized")
	@ResponseBody
	public PageInfo<Acredit> unauthorizedDevListData(User user,Acredit acredit,HttpServletRequest request,
			HttpServletResponse response) {
		    PageInfo<Acredit> pageInfo;
       
            	acredit.setUserid(user.getId());
    			List<Acredit> list = acreditService.getDevList(acredit);
    			
    			ArrayList<String> arrayList = new ArrayList<>();
    			for (Acredit acreditValue : list) {				
    				arrayList.add(acreditValue.getDevid());
    			}
    			System.out.println(arrayList.size());
    			if(arrayList.size() != 0) {
    				startPage(request);
        			List<Acredit> acreditList = acreditService.getUnauthorizedDevList(arrayList);
        			acreditList = ToolUtil.getFiltrateUnauthorizedDevList(acreditList, acredit);
        			pageInfo = new PageInfo<Acredit>(acreditList);
    			}else {
    				startPage(request);
                	List<Acredit> acreditList = acreditService.getUnauthorizedDevListWhereIdisNull(acredit);
                	pageInfo = new PageInfo<Acredit>(acreditList);
    			}

			return pageInfo;
	}

	// 将新添加接受短信的设备保存到数据库
	@RequestMapping(value = "addNewAuthorizedDev")
	@ResponseBody
	public PageInfo<Acredit> addNewAuthorizedDev(Acredit acredit,User user,HttpServletRequest request) {

		if (!acredit.getDevid().equals("") && (user.getId() != null)) {
			String[] split = acredit.getDevid().split(",");
			acreditService.saveNewDev(split, dse, dseService,acredit,user,acreditService);
			
			acredit.setUserid(user.getId());
			startPage(request);
			List<Acredit> acreditList = acreditService.getDevList(acredit);
			PageInfo<Acredit> pageInfo = new PageInfo<Acredit>(acreditList);
			return pageInfo;
		}
		return null;
	}
}
