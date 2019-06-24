package com.caiger.module.sys.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.caiger.common.service.CrudService;
import com.caiger.module.sys.dao.DseDao;
import com.caiger.module.sys.entity.Dse;

@Service
public class DseService extends CrudService<DseDao, Dse> {

	@Autowired
	private DseDao dseDao;

	public List<Dse> getStatusList(HashMap<String, String> dicMap,Dse dse) {

		List<Dse> statusList = dseDao.getStatusList(dse);

		for (Dse dse2 : statusList) {
			dse2.setMainStatus(dicMap.get(dse2.getMainStatus()));
			dse2.setStatusIden(dicMap.get(dse2.getStatusIden()));
			dse2.setMalIden(dicMap.get(dse2.getMalIden()));
			dse2.setBmsIden(dicMap.get(dse2.getBmsIden()));
			dse2.setPtNeedle(dicMap.get(dse2.getPtNeedle()));
			dse2.setBlackOutAlarm(dicMap.get(dse2.getBlackOutAlarm()));
		}
		return statusList;
	}

}
