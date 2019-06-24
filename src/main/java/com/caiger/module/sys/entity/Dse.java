package com.caiger.module.sys.entity;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.caiger.common.entity.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Component("dse")
@Scope(scopeName="prototype")
@Getter
@Setter
public class Dse extends DataEntity<Dse> {

	private static final long serialVersionUID = 1L;

	private String id;
	private String devName;   // 设备名称
	private String devNumber;    // 设备编号
	private String devLocation;  // 设备地址
	private String guc;       // 硬件编码
	private String emaddress; // 电表地址
	private String quantity; // 当前电量
	private String voltage; // 当前电压
	private String current; // 当前电流
	private String power; // 当前功率
	private String softver; // 开关电源软件版本
	private String hardver; // 开关电源硬件版本
	private String ratedPower; // 额定功率
	private String mainStatus; // 主运行状态
	private String statusIden; // 状态标志组
	private String malIden; // 故障标志组
	// 电源温度
	private String powerTemperature;
	// AC输入功率
	private String acInputPower;
	// 输入电压
	private String outputVoltage;
	// 输出电流
	private String outputCurrent;
	// 电池电流
	private String batteryCurrent;
	// 加热电流
	private String heaterCurrent;
	// 负载电流
	private String loadCurrent;
	// 电池温度
	private String batteryTemperature;
	// 目标电压
	private String targetVoltage;
	// 额定电流
	private String ratedCurrent;
	// 充电限流
	private String currentLimit;
	// 电池放电时间
	private String batteryDischargeTime;
	// 电池电量
	private String batteryQuantity;
	// BMS标识
	private String bmsIden;
	// 防雷针
	private String ptNeedle;
	// 停电告警
	private String blackOutAlarm;

	// 是否在线
	private String onlineFlag;

	private Date createDate;
	
	private String dev_id;
	
//	private List<Dse> dseList = Lists.newArrayList();

	public Dse() {

	}

	public Dse(String id) {
		super(id);
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

}
