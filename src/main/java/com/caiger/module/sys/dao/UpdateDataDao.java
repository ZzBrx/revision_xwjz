package com.caiger.module.sys.dao;

import java.util.List;

import com.caiger.common.dao.CrudDao;
import com.caiger.module.sys.entity.UpdateData;
import com.caiger.module.sys.entity.UpdateFlag;

public interface UpdateDataDao extends CrudDao<UpdateData>{

	public List<UpdateData> getUpdateDataByFileId(UpdateData updateData);

	public UpdateData getUpdateDataByFileIdAndPackageNo(UpdateFlag updateFlagInfo);

}
