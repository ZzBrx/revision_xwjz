package com.caiger.module.sys.dao;

import java.util.List;
import com.caiger.common.dao.CrudDao;
import com.caiger.module.sys.entity.AlarmInfo;

public interface AlarmInfoDao extends CrudDao<AlarmInfo> {
	public List<AlarmInfo> getHistoryStatusList(AlarmInfo alarmInfo);
	public List<AlarmInfo> getCurrentStatusList(AlarmInfo alarmInfo);
}
