package com.caiger.common.dao;
import java.util.List;

/**
 * @className: CrudDao
 * @description: 基础数据持久层操作支持类
 * @author: 黄凯杰
 * @date: 2019年2月26日 上午11:07:22
 * @version: V1.0.0
 * @copyright: Copyright © 2019 Caiger Digital Technology co., Ltd.
 */
public interface CrudDao<T> extends BaseDao {

	/**
	 * @methodName: insert
	 * @description: 插入数据
	 * @param entity
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:16:21
	 */
	public int insert(T entity);

	/**
	 * @methodName: delete
	 * @description: 根据唯一编号删除数据
	 * @param id 唯一编号
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:47:02
	 */
	public int delete(String id);

	/**
	 * @methodName: delete
	 * @description: 删除数据
	 * @param entity 实体
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:47:02
	 */
	public int delete(T entity);
	
	public int delete_status(T entity);
	
	public int delete_mal(T entity);
	
	public int delete_bms(T entity);

	/**
	 * @methodName: update
	 * @description: 更新数据
	 * @param entity
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:16:38
	 */
	public int update(T entity);

	/**
	 * @methodName: get
	 * @description: 根据唯一编号查询单条数据
	 * @param id 唯一编号
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:16:56
	 */
	public T get(String id);

	/**
	 * @methodName: get
	 * @description: 根据条件查询单条数据
	 * @param id 唯一编号
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:16:56
	 */
	public T get(T entity);

	/**
	 * @methodName: getList
	 * @description: 根据条件查询数据列表
	 * @param entity 实体
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月26日 下午9:17:50
	 */
	public List<T> getList(T entity);
	
	public List<T> getList();
    
	/**
	 * @methodName: getAllList
	 * @description: 为超级管理员时获得所有数据列表
	 * @param entity
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年3月6日 下午4:11:33
	 */
	public List<T> getAllList(T entity);

//	public List<User_dev> getDevList(T entity);
	
	public List<T> getOtherInfoList(T entity);
	
	public T getDseByAddress(T entity);
	
	public void updateDseOnlineById(T entity);
	
	public void updateById(T entity);
	
	public void saveDse(T entity);
	
	public void saveStatus(T entity);
	
	public List<T> getAllDseList(T entity);
	
	public void deleteBatches(List<String> list);
	
}
