package com.caiger.module.sys.entity;

import com.caiger.common.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MalIden extends DataEntity<MalIden> {

	private static final long serialVersionUID = 1L;

	private String id;
	private String dev_id; // dev_dse表的主键
	private String mald0;
	private String mald1;
	private String mald2;
	private String mald3;
	private String mald4;
	private String mald5;
	private String mald6;
	private String mald7;
	private String mald8;
	private String mald9;
	private String mald10;
	private String mald11;
	
	public MalIden() {
	}
	
	public MalIden(String id) {
		super(id);
	}

}

