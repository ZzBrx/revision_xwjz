package com.caiger.module.sys.shiro.realm;

import org.apache.shiro.SecurityUtils;
import com.blade.ioc.annotation.Component;

@Component("DictUtils")
public class PermissionUtils {
	public Boolean hasPermi(String permission) {
		return SecurityUtils.getSubject().isPermitted(permission);
	}

}
