package com.caiger.module.sys.dao;

import java.util.List;

import com.caiger.common.dao.CrudDao;
import com.caiger.module.sys.entity.Dictionaries;

public interface DicDao extends CrudDao<Dictionaries>{
		
	public List<Dictionaries> getDicList();

}
