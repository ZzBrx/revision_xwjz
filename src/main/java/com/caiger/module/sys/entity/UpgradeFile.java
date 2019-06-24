package com.caiger.module.sys.entity;

import java.util.Date;
import com.caiger.common.entity.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpgradeFile extends DataEntity<UpgradeFile>{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String fileName;
	private String url;
	private String type;
	private Date createDate;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getCreateDate() {
		return createDate;
	}
}
