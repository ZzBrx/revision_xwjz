package com.caiger.module.sys.entity;

import java.io.Serializable;
import java.net.DatagramSocket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecMsgEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private DatagramSocket datagramSocket;
	private String ip;
	private int port;
	private String fh;
	private String ver;
	private String sn;
	private String addressInfo;
	private String cmdCode;
	private String statusCode;
	private String body;
	private String checkBit;
	private String ft;

}
