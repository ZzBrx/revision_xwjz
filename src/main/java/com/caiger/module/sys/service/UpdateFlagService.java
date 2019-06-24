package com.caiger.module.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caiger.common.service.CrudService;
import com.caiger.module.sys.dao.UpdateFlagDao;
import com.caiger.module.sys.entity.UpdateFlag;

@Service
public class UpdateFlagService extends CrudService<UpdateFlagDao, UpdateFlag> {
	
	@Autowired
	private UpdateFlagDao updateFlagDao;

	public UpdateFlag getUpdateFlagByDseId(UpdateFlag updateFlagInfo) {
		return updateFlagDao.getUpdateFlagByDseId(updateFlagInfo);
	}
    
	@Transactional(readOnly=false)
	public void updateFlagByDseId(UpdateFlag updateFlagInfo) {
		updateFlagDao.updateFlagByDseId(updateFlagInfo);
	}
	
	@Transactional(readOnly=false)
	public void updatePackageNoByDseId(UpdateFlag updateFlagInfo) {
		updateFlagDao.updatePackageNoByDseId(updateFlagInfo);
	}

	public List<UpdateFlag> getUpdateFlag() {
		return updateFlagDao.getUpdateFlag();
	}
    
	@Transactional(readOnly=false)
	public void deleteUpdateFlagByDseId(UpdateFlag updateFlag) {
		updateFlagDao.deleteUpdateFlagByDseId(updateFlag);	
	}
	
	@Transactional(readOnly=false)
	public void saveUpdateFlag(UpdateFlag updateFlag) {
		updateFlagDao.saveUpdateFlag(updateFlag);
	}

}
