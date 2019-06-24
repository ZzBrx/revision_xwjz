package com.caiger.module.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caiger.common.service.CrudService;
import com.caiger.module.sys.dao.UpgradeFileDao;
import com.caiger.module.sys.entity.UpgradeFile;

@Service
public class UpgradeService extends CrudService<UpgradeFileDao, UpgradeFile> {

	@Autowired
	private UpgradeFileDao upgradeFileDao;

	public UpgradeFile getUpdateFileByName(UpgradeFile upgradeFile) {
		return upgradeFileDao.getUpdateFileByName(upgradeFile);
	}
	
	public void saveUpdateData() {
		
	}
}
