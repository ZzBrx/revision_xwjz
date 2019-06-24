package com.caiger.module.sys.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PowerStatus implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String powerTemp;
	private String acInputPower;
	private String retain;
	private String outputVoltage;
	private String outputElectricity;
	private String batteryElectricity;
	private String heatElectricity;
	private String loadElectricity;
	private String batteryTemp;
	private String targetVoltage;
	private String ratedElectricity;
	private String limitCharge;
	private String batteryDischargetime;
}
