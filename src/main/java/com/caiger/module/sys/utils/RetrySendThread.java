package com.caiger.module.sys.utils;

import java.net.DatagramSocket;
import java.util.Date;
import com.caiger.module.sys.entity.SendMsgEntity;

public class RetrySendThread extends Thread {
	
	private SendMsgEntity sendMsgEntity;
	private DatagramSocket datagramSocket;
	private boolean retryFlag = false;
	private boolean sendFlag = false;
	private int count = 0;
	private long startTime;
	
	public RetrySendThread() {
	}
	
	public void cancel(){
		count = 0;
		retryFlag = false;
		sendFlag = false;
		
	}
	
	
	public RetrySendThread(DatagramSocket datagramSocket,SendMsgEntity sendMsgEntity,boolean retryFlag,Date startDate) {
		System.out.println("定时器开始....");
		this.datagramSocket = datagramSocket;
		this.sendMsgEntity = sendMsgEntity;
		this.retryFlag = retryFlag;
		count = 0;
		startTime = startDate.getTime();
	}
	
	@Override
	public void run() {
		
		while (retryFlag) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("等待开始......");
			long nowTime = new Date().getTime();
			//时间过去十秒
			if (nowTime-startTime>=10*1000) {
				System.out.println("发送flag开启....");
				retryFlag = false;
				sendFlag = true;
			}
		}
		while(!retryFlag&&sendFlag){
			try {
			count++;
			System.out.println("定时器发送.....");
			System.out.println("定时器发送第"+count+"条");
			SendMessage.send(datagramSocket, sendMsgEntity);
			sleep(10000);
			if (count>=3) {
				System.out.println("定时器停止....");
				
				sendFlag = false;
			}	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}