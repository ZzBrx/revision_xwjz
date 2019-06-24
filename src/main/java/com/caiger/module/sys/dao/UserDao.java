package com.caiger.module.sys.dao;

import java.util.List;

import com.caiger.common.dao.CrudDao;
import com.caiger.module.sys.entity.Acredit;
import com.caiger.module.sys.entity.User;


/**
 * @className:  UserDao   
 * @description: 用户DAO实现   
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:09:08
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
public interface UserDao extends CrudDao<User>  {

	/**
	 * @methodName: getByUsername   
	 * @description: 根据用户名查询用户   
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月27日 上午8:56:31
	 */
	public User getByUsername(User user);
	
	/**
	 * @methodName: getUserByOrgId   
	 * @description:  根据组织机构编号获取用户列表
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月27日 上午8:56:51
	 */
	public List<User> getUserByOrgId(User user);
	
	/**
	
	/**
	 * @methodName: insertUserRole   
	 * @description: 插入用户与角色关联   
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月27日 上午8:58:01
	 */
	public int insertUserRole(User user);
	
	
	/**
	 * @methodName: deleteUserPost   
	 * @description: 删除用户岗位关联表  
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午3:10:50
	 */
	public int deleteUserPost(User user);
	
	/**
	 * @methodName: insertUserPost   
	 * @description: 插入用户岗位关联表
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午3:11:16
	 */
	public int insertUserPost(User user);
	
	
	
	/**
	 * @methodName: deleteUserPost   
	 * @description: 删除用户组织机构关联表 （数据范围权限）
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午3:10:50
	 */
	public int deleteUserOrg(User user);
	
	/**
	 * @methodName: insertUserPost   
	 * @description: 插入用户组织机构关联表（数据范围权限）
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午3:11:16
	 */
	public int insertUserOrg(User user);
	
	
	
	
	
	/**
	 * @methodName: updatePasswordById   
	 * @description: 改密   
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月27日 下午1:09:26
	 */
	public int updatePasswordById(User user);
	
	/**
	 * @methodName: updateLoginInfo   
	 * @description: 更新用户登录信息   
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月27日 上午8:58:34
	 */
	public int updateLoginInfo(User user);
	
	
	/**
	 * @methodName: getByCode   
	 * @description: 根据员工编号查询   
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午1:40:29
	 */
	public User getByCode(User user);
	
	/**
	 * @methodName: updateStatus   
	 * @description: 更新用户状态  
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月12日 上午11:02:34
	 */
	public int updateStatus(User user);
	
	
	/**
	 * @methodName: deleteUserRole   
	 * @description: 删除用户角色关联 
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月12日 下午4:34:54
	 */
	public int deleteUserRole(User user);
	
	
	public List<User> getUserByDevid(Acredit Acredit);
	
}
