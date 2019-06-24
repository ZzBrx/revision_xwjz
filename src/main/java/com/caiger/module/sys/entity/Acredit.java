package com.caiger.module.sys.entity;
import org.springframework.stereotype.Component;

import com.caiger.common.entity.DataEntity;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class Acredit extends DataEntity<Acredit>{
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String userid;
	
	private String devid;
	
	private String devName;
	
	private String devNumber;
	
	private String devLocation;
	
	private String emaddress;

}
