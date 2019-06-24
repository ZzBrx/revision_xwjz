package com.caiger.module.sys.entity;

import java.util.List;
import com.caiger.common.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @className: User
 * @description: 用户
 * @author: 黄凯杰
 * @date: 2018年12月26日 下午10:11:46
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Getter
@Setter
public class User extends DataEntity<User> {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	private String username; // 登录账户

	private String oldUsername;// 用于修改用户是登录名检测

	private String userCode;// 用户编码

	private String oldUserCode;// 用于用户工号编码检测

	private String pd;

	private String password;// 加密后的密码

	private String tel; // 电话号码

	private String salt;// 盐

	private List<Dse> dseList;

	private String id;

	public User() {

	}

	public User(String id) {
		super(id);
		this.id = id;
	}

	public User(String id, String username) {
		super(id);
		this.username = username;
	}

}
