package com.caiger.module.sys.dao;

import java.util.List;
import com.caiger.common.dao.CrudDao;
import com.caiger.module.sys.entity.Acredit;

public interface AcreditDao extends CrudDao<Acredit>{
	
	public List<Acredit> getDevList(Acredit acredit);
	
	public List<Acredit> getUnauthorizedDevList(List<String> list);
	
	public List<Acredit> getUnauthorizedDevListWhereIdisNull(Acredit acredit);
	
	public void saveNewAuthorizedDev(Acredit acredit);

}
