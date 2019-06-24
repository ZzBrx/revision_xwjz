package com.caiger.module.sys.dao;

import java.util.List;

import com.caiger.common.dao.CrudDao;
import com.caiger.module.sys.entity.Dse;

public interface DseDao extends CrudDao<Dse> {

	public List<Dse> getStatusList(Dse dse);
}
