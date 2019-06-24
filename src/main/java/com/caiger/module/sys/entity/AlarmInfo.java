package com.caiger.module.sys.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.caiger.common.entity.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

@Component
@Scope(scopeName="prototype")
@Getter
@Setter
public class AlarmInfo extends DataEntity<AlarmInfo>{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String dev_id;
	private String alarmType;
	private String alarmInfo;
	private Date startTime;
	private String alarmFlag;
	private Date endTime;
	private Dse dse;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getStartTime() {
		return startTime;	
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getEndTime() {
		return endTime;	
	}
	
}
