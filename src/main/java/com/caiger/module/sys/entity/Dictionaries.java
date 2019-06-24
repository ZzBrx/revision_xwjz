package com.caiger.module.sys.entity;

import com.caiger.common.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dictionaries extends DataEntity<Dictionaries>{
	
	private static final long serialVersionUID = 1L;
	
	private String dickey;
	
	private String dictype;
}
