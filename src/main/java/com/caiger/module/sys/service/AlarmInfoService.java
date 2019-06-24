package com.caiger.module.sys.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.caiger.common.service.CrudService;
import com.caiger.module.sys.dao.AlarmInfoDao;
import com.caiger.module.sys.entity.AlarmInfo;

@Service
public class AlarmInfoService extends CrudService<AlarmInfoDao, AlarmInfo> {
	@Autowired
	private AlarmInfoDao alarmInfoDao;

	public List<AlarmInfo> getStatusList(String historyOrCurrent,HashMap<String, String> dicMap, AlarmInfo alarmInfo) {

		List<AlarmInfo> statusList = null;
        
		if (historyOrCurrent.equals("history")) {
			statusList = alarmInfoDao.getHistoryStatusList(alarmInfo);
		}else if(historyOrCurrent.equals("current")) {
			statusList = alarmInfoDao.getCurrentStatusList(alarmInfo);
		}

		if (statusList != null) {
			for (AlarmInfo alarmList : statusList) {
				alarmList.setAlarmType(dicMap.get(alarmList.getAlarmType()));
			}
		}
		return statusList;
	}
}
