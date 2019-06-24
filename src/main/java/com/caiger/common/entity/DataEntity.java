package com.caiger.common.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * @className:  DataEntity   
 * @description: 数据实体   
 * @author: 黄凯杰 
 * @date: 2019年2月25日 下午4:19:04
 * @version: V1.0.0
 * @copyright: Copyright © 2019 Caiger Digital Technology co., Ltd.
 */

@Getter
@Setter
public class DataEntity<T> extends BaseEntity  {
	
	/**
	 * @fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String name;//数据实体名称
	
	protected String oldName;//实体名称 用于更新时验证
	
	public DataEntity() {
		
	}
	
	public DataEntity(String id) {
		super(id);
	}


	/*@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}*/

	
	
	
}
