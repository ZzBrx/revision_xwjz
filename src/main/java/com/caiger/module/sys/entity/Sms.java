package com.caiger.module.sys.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sms implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String devname;
	private String devid;
	private String devlocation;
	private String address;
	private String status;
	private String date;

}
