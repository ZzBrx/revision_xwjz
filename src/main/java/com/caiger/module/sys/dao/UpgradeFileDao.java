package com.caiger.module.sys.dao;

import com.caiger.common.dao.CrudDao;
import com.caiger.module.sys.entity.UpgradeFile;

public interface UpgradeFileDao extends CrudDao<UpgradeFile>{
	public UpgradeFile getUpdateFileByName(UpgradeFile upgradeFile);

}
