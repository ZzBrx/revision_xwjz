package com.caiger.module.sys.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.caiger.common.service.CrudService;
import com.caiger.module.sys.dao.UpdateDataDao;
import com.caiger.module.sys.entity.UpdateData;
import com.caiger.module.sys.entity.UpdateFlag;

@Service
public class UpdateDataService extends CrudService<UpdateDataDao, UpdateData> {
	
	@Autowired
	private UpdateDataDao updateDataDao;
	
	public List<UpdateData> getUpdateDataByFileId(UpdateData updateData) {
		return updateDataDao.getUpdateDataByFileId(updateData);		
	}

	public UpdateData getUpdateDataByFileIdAndPackageNo(UpdateFlag updateFlagInfo) {
		return updateDataDao.getUpdateDataByFileIdAndPackageNo(updateFlagInfo);
	}

}
