package com.caiger.module.sys.entity;

import com.caiger.common.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFlag extends DataEntity<UpdateFlag>{

	private static final long serialVersionUID = 1L;

	private String dse_id;

	private String file_id;

	private int packageNo;
	
	private int package_no;

	private int packageSize;

	private String flag;
}
