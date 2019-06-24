package com.caiger.module.sys.entity;

import com.caiger.common.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BmsIden extends DataEntity<BmsIden> {

	private static final long serialVersionUID = 1L;

	private String id;
	private String dev_id; // dev_dse表的主键
	private String bmsd0;
	private String bmsd1;
	private String bmsd2;
	private String bmsd3;
	private String bmsd4;
	private String bmsd5;
	private String bmsd6;
	private String bmsd7;
	private String bmsd8;
	private String bmsd9;
	private String bmsd10;
	private String bmsd11;
	private String bmsd12;
	private String bmsd13;

	public BmsIden() {
	}

	public BmsIden(String id) {
		super(id);
	}

}
