package com.caiger.module.sys.entity;

import com.caiger.common.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateData extends DataEntity<UpdateData>{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String file_id;
	private Integer package_no;
	private String data;
	
}
