package com.caiger.module.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caiger.common.service.CrudService;
import com.caiger.module.sys.dao.UserDao;
import com.caiger.module.sys.entity.Acredit;
import com.caiger.module.sys.entity.User;

/**
 * @className:  UserSevice   
 * @description: 用户服务  
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午10:16:44
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Service
@Transactional(readOnly = true)
public class UserService extends CrudService<UserDao,User> {
	
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * @methodName: getByUsername   
	 * @description:根据用户名查询   
	 * @param username
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午1:41:40
	 */
	public User getByUsername(String username) {
		return userDao.getByUsername(new User(null,username));
	}
	

	/**
	 * @methodName: getByCode   
	 * @description: 根据用户编号查询 
	 * @param user
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月11日 下午1:41:27
	 */
	public User getByCode(User user) {
		return userDao.getByCode(user);
	}
	
	/**
	 * @methodName: delete   
	 * @description: 删除用户    
	 * @param id   
	 * @see com.caiger.common.service.CrudService#delete(java.lang.String)
	 */
	@Transactional(readOnly = false)
	@Override
	public void delete(User user) {
		super.delete(user);
	}
	
	/**
	 * @methodName: updatePasswordById   
	 * @description: 更新密码   
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月12日 下午2:22:03
	 */
	@Transactional(readOnly = false)
	public int updatePasswordById(User user) {
		return userDao.updatePasswordById(user);
	}
	
	public List<User> getUserByDevid(Acredit Acredit){
		return userDao.getUserByDevid(Acredit);
	}
}
