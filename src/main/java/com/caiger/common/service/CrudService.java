package com.caiger.common.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.caiger.common.dao.CrudDao;
import com.caiger.common.entity.DataEntity;
import com.caiger.common.lang.StringUtils;
import com.caiger.common.utils.IdGenUtils;

/**
 * @className: CrudService
 * @description: 基础数据service支持类
 * @author: 黄凯杰
 * @date: 2019年2月26日 上午11:24:08
 * @version: V1.0.0
 * @copyright: Copyright © 2019 Caiger Digital Technology co., Ltd.
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity<T>> extends BaseService {

	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;

	/**
	 * @methodName: get
	 * @description: 根据条件获取单条数据
	 * @param entity
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月26日 上午11:18:41
	 */
	public T get(T entity) {
		return dao.get(entity);
	}

	/**
	 * @methodName: getList
	 * @description: 查询数据列表
	 * @param entity 数据实体
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年1月2日 下午8:54:20
	 */
	public List<T> getList(T entity) {
		return dao.getList(entity);
	}
	
	public List<T> getList() {
		return dao.getList();
	}

	public List<T> getAllDseList(T entity) {
		return dao.getAllDseList(entity);
	}
	
//	public List<User_dev> getDevList(T entity) {
//		return dao.getDevList(entity);
//	}

	public List<T> getOtherInfoList(T entity) {
		return dao.getOtherInfoList(entity);
	}

	public T getDseByAddress(T entity) {
		return dao.getDseByAddress(entity);
	}

	@Transactional(readOnly = false)
	public void saveDse(T entity) {
		dao.saveDse(entity);
	}

	@Transactional(readOnly = false)
	public void savestatus(T entity) {
		dao.saveStatus(entity);
	}


	/**
	 * @methodName: getAllList
	 * @description: 当管理员类型为超级管理员时获得所有数据
	 * @param entity
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月6日 下午4:10:10
	 */
	public List<T> getAllList(T entity) {
		return dao.getAllList(entity);
	}

	@Transactional(readOnly = false)
	public void updateDseOnlineById(T entity) {
		dao.updateDseOnlineById(entity);
	}

	@Transactional(readOnly = false)
	public void updateById(T entity) {
		dao.updateById(entity);
	}

	/**
	 * @methodName: delete
	 * @description: 根据唯一编号删除数据
	 * @param id 唯一编号
	 * @author: 黄凯杰
	 * @date: 2019年1月2日 下午8:56:38
	 */
	@Transactional(readOnly = false)
	public void delete(String id) {
		dao.delete(id);
	}

	/**
	 * @methodName: delete
	 * @description: 删除数据
	 * @param entity 实体对象
	 * @author: 黄凯杰
	 * @date: 2019年3月6日 上午9:11:15
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		dao.delete(entity);
	}

	/**
	 * 保存
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void save(T entity) {
		if (StringUtils.isNotBlank(entity.getId())) {
			dao.update(entity);
		} else {
			entity.setId(IdGenUtils.uuid());
			dao.insert(entity);
		}
	}

	@Transactional(readOnly = false)
	public void deleteAll(T entity) {
		dao.delete(entity);
		dao.delete_status(entity);
		dao.delete_mal(entity);
		dao.delete_bms(entity);
		
	}
	
	@Transactional(readOnly = false)
    public void deleteBatches(List<String> list) {
    	dao.deleteBatches(list);
    }

}
