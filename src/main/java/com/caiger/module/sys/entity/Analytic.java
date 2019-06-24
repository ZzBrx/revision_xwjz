package com.caiger.module.sys.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Analytic implements Serializable{
  	
	private static final long serialVersionUID = 1L;
	
	private String emaddress;
	private String quantity;
	private String voltage;
	private String current;
	private String power;
	private String softver;
	private String hardver;
	private String ratedpower;
	private String mainstatus;
	private String statusiden;
	private String failureiden;
	private String bmsiden;
	private Map<String, Map<String, String>> statusMap = new HashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> malMap = new HashMap<String, Map<String, String>>();
	// 电源温度
	private String powerTemperature;
	// AC输入功率
	private String acInputPower;
	// 输出电压
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
	private Map<String, Map<String, String>> bmsMap = new HashMap<String, Map<String, String>>();
	// 防雷器
	private String ptNeedle;
	// 停电告警
	private String blackoutAlarm;

//	private String createdate;
}
