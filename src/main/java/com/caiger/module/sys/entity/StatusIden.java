package com.caiger.module.sys.entity;

import com.caiger.common.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusIden extends DataEntity<StatusIden> {

	private static final long serialVersionUID = 1L;

	private String id;
	private String dev_id; // dev_dse表的主键
	private String statusd0;
	private String statusd1;
	private String statusd2;
	private String statusd3;
	private String statusd4;
	private String statusd5;
	private String statusd6;
	private String statusd7;
	private String statusd8;
	
	public StatusIden() {
	}
	
	public StatusIden(String id) {
		super(id);
	}

}
