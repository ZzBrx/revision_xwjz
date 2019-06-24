package com.caiger.module.sys.dao;

import java.util.List;

import com.caiger.common.dao.CrudDao;
import com.caiger.module.sys.entity.UpdateFlag;

public interface UpdateFlagDao extends CrudDao<UpdateFlag>{

	public UpdateFlag getUpdateFlagByDseId(UpdateFlag updateFlagInfo);

	public void updateFlagByDseId(UpdateFlag updateFlagInfo);

	public void updatePackageNoByDseId(UpdateFlag updateFlagInfo);

	public List<UpdateFlag> getUpdateFlag();

	public void deleteUpdateFlagByDseId(UpdateFlag updateFlag);

	public void saveUpdateFlag(UpdateFlag updateFlag);

}
