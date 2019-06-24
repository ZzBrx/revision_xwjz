package com.caiger.module.sys.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.caiger.common.service.CrudService;
import com.caiger.module.sys.dao.DicDao;
import com.caiger.module.sys.entity.Dictionaries;

@Service
@Transactional(readOnly = true)
public class DicService extends CrudService<DicDao, Dictionaries> {

	@Autowired
	private DicDao dicDao;

	public HashMap<String, String> queryAll() {
		HashMap<String, String> hashMap = new HashMap<>();
		List<Dictionaries> dicList = dicDao.getDicList();
		for (Dictionaries dic : dicList) {
			hashMap.put(dic.getDickey(), dic.getDictype());
		}
		return hashMap;
	}

}
